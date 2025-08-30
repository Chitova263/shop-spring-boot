package com.chitova.florist.services;

import com.chitova.florist.domain.cart.Cart;
import com.chitova.florist.domain.cart.CartItem;
import com.chitova.florist.model.checkout.request.CheckoutCartRequest;
import com.chitova.florist.model.checkout.request.UpdateCartRequest;
import com.chitova.florist.model.checkout.response.CartResponse;
import com.chitova.florist.outbound.checkout.ElasticPathCloudCheckoutClient;

import com.chitova.florist.outbound.checkout.model.*;
import com.chitova.florist.repositories.CartRepository;
import com.chitova.florist.services.identity.IdentityUserService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CheckoutService {

    public static final String CUSTOM_CART_DESCRIPTION = "CustomCartDescription";
    public static final String CUSTOM_CART_NAME = "CustomCartName";
    private final ElasticPathCloudCheckoutClient elasticPathCloudCheckoutClient;
    private final CartRepository cartRepository;
    private final IdentityUserService identityUserService;

    public CheckoutService(final ElasticPathCloudCheckoutClient elasticPathCloudCheckoutClient,
                           final CartRepository cartRepository,
                           final IdentityUserService identityUserService) {
        this.elasticPathCloudCheckoutClient = elasticPathCloudCheckoutClient;
        this.cartRepository = cartRepository;
        this.identityUserService = identityUserService;
    }

    public CartResponse addItemToCart(UpdateCartRequest cartRequest) {
        final Cart cart = cartRepository.findByCustomerId(identityUserService.getIdentityUser().sub())
                .orElse(null);
        if (Objects.isNull(cart)) {
            return createNewCart(cartRequest);
        }
        return addItemToExistingCart(cartRequest, cart);
    }

    public CartResponse updateCart(final UpdateCartRequest updateCartRequest) {
        if (Objects.isNull(updateCartRequest.getCartId())) {
            final Cart cart = Cart.builder()
                    .customerId(identityUserService.getIdentityUser().sub())
                    .cartLineItems(CheckoutService.toCartLineItems(updateCartRequest.getLineItems()))
                    .build();

            final Cart updatedCart = cartRepository.save(cart);
            return CartResponse.builder()
                    .cartId(updatedCart.getId().toString())
                    .cartItems(CheckoutService.toCartLineItemsResponse(updatedCart.getCartLineItems()))
                    .build();
        }

        // Update the existing cart
        final List<CartItem> items = updateCartRequest.getLineItems()
                .stream().map(cartItem -> CartItem.builder()
                    .sku(cartItem.getSku())
                    .quantity(cartItem.getQuantity())
                    .build())
                .collect(Collectors.toList());
        final Cart cart = cartRepository.findById(new ObjectId(updateCartRequest.getCartId()))
                .orElseThrow();

        cart.setCartLineItems(items);
        final Cart updatedCart = cartRepository.save(cart);
        return CartResponse.builder()
                .cartId(updatedCart.getId().toString())
                .cartItems(CheckoutService.toCartLineItemsResponse(updatedCart.getCartLineItems()))
                .build();
    }


    private CartResponse addItemToExistingCart(final UpdateCartRequest cartRequest, final Cart existingCart) {
        final Map<String, UpdateCartRequest.CartLineItem> skuToAddedLineItem = cartRequest.getLineItems().stream()
                .collect(Collectors.toMap(UpdateCartRequest.CartLineItem::getSku, item -> item));

        final Map<String, CartItem> existingSkuToLineItem = existingCart.getCartLineItems().stream()
                .collect(Collectors.toMap(CartItem::getSku, item -> item));

        final var newCartLineItems = cartRequest.getLineItems().stream()
                .map(lineItem -> {
                    if (!skuToAddedLineItem.containsKey(lineItem.getSku())) {
                        return CartItem.builder()
                                .quantity(lineItem.getQuantity())
                                .sku(lineItem.getSku())
                                .build();
                    } else {
                        return CartItem.builder()
                                .quantity(CheckoutService.getUpdatedLineItemQuantity(lineItem, existingSkuToLineItem))
                                .sku(lineItem.getSku())
                                .build();
                    }
                })
                .toList();

        final var unModifiedExistingCartItems =  existingCart.getCartLineItems().stream()
                .filter(cartItem -> !skuToAddedLineItem.containsKey(cartItem.getSku()))
                .toList();

        final List<CartItem> updatedCartLineItems = Stream.concat(newCartLineItems.stream(), unModifiedExistingCartItems.stream())
                .toList();
        existingCart.setCartLineItems(updatedCartLineItems);
        final Cart updatedCart = cartRepository.save(existingCart);
        return CartResponse.builder()
                .cartId(updatedCart.getId().toString())
                .cartItems(toCartLineItemsResponse(updatedCart.getCartLineItems()))
                .build();
    }

    private CartResponse createNewCart(UpdateCartRequest cartRequest) {
        final Cart newCart = Cart.builder()
                .customerId(identityUserService.getIdentityUser().sub())
                .cartLineItems(CheckoutService.toCartLineItems(cartRequest.getLineItems()))
                .build();
        final Cart updatedCart = cartRepository.save(newCart);
        return CartResponse.builder()
                .cartId(updatedCart.getId().toString())
                .cartItems(toCartLineItemsResponse(updatedCart.getCartLineItems()))
                .build();
    }

    private static int getUpdatedLineItemQuantity(UpdateCartRequest.CartLineItem lineItem, Map<String, CartItem> skuToAddedLineItem) {
        return Optional.ofNullable(skuToAddedLineItem.get(lineItem.getSku()))
                .map(cartItem -> cartItem.getQuantity() + lineItem.getQuantity())
                .orElse(lineItem.getQuantity());
    }

    private static List<CartResponse.CartItem> toCartLineItemsResponse(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> CartResponse.CartItem.builder()
                        .quantity(cartItem.getQuantity())
                        .sku(cartItem.getSku())
                        .build())
                .collect(Collectors.toList());
    }


    public CartResponse checkout(final CheckoutCartRequest checkoutCartRequest, final String accountManagementToken) {
        // Fetch Cart Existing
        Cart cart = cartRepository.findById(new ObjectId(checkoutCartRequest.getCartId())).orElseThrow();

        String cartId = cart.getElasticPathCloudCartId();
        if(Objects.isNull(cartId)) {
            final ElasticPathCloudCreateCustomCartRequest createCustomCartRequest = getCreateCustomCartRequest(identityUserService.getIdentityUser().email());
            final ElasticPathCloudCreateCustomCartResponse createCustomCartResponse = elasticPathCloudCheckoutClient.createCustomCart(createCustomCartRequest);
            cartId = createCustomCartResponse.getData().getId();
        }

        final ElasticPathCloudBulkAddItemsToCartRequest bulkAddItemsToCartRequest = getBulkAddItemsToCartRequest(cart);
        final ElasticPathCloudBulkAddItemsToCartResponse bulkAddItemsToCartResponse = elasticPathCloudCheckoutClient.bulkAddItemsToCart(cartId, bulkAddItemsToCartRequest);

        return CartResponse.builder()
                .cartId(cartId)
                .cartItems(bulkAddItemsToCartRequest.getData()
                        .stream()
                        .map(customCartItem -> CartResponse.CartItem.builder().build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<CartItem> toCartLineItems(final List<UpdateCartRequest.CartLineItem> cartLineItems) {
        return cartLineItems.stream()
                .map(cartItem -> CartItem.builder()
                        .quantity(cartItem.getQuantity())
                        .sku(cartItem.getSku())
                        .build())
                .collect(Collectors.toList());
    }

    private static ElasticPathCloudBulkAddItemsToCartRequest getBulkAddItemsToCartRequest(Cart cart) {
        return ElasticPathCloudBulkAddItemsToCartRequest
                .builder()
                .data(cart.getCartLineItems().stream()
                        .map(cartItem -> ElasticPathCloudBulkAddItemsToCartRequest.Item.builder()
                                .sku(cartItem.getSku())
                                .quantity(cartItem.getQuantity())
                                .type("cart_item")
                                .build())
                        .collect(Collectors.toList())
                )
                .build();
    }

    private ElasticPathCloudCreateCustomCartRequest getCreateCustomCartRequest(final String email) {
        return ElasticPathCloudCreateCustomCartRequest.builder()
                .data(ElasticPathCloudCreateCustomCartRequest.DataPayload.builder()
                        .description(CUSTOM_CART_DESCRIPTION)
                        .name(CUSTOM_CART_NAME)
                        .discountSettings(ElasticPathCloudCreateCustomCartRequest.DiscountSettings.builder()
                                .customDiscountsEnabled(false)
                                .build())
                        .contact(ElasticPathCloudCreateCustomCartRequest.Contact.builder()
                                .email(email)
                                .build())
                        .build())
                .build();
    }
}
