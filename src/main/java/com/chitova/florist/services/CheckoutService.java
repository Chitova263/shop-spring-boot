package com.chitova.florist.services;

import com.chitova.florist.entities.cart.Cart;
import com.chitova.florist.entities.cart.CartItem;
import com.chitova.florist.model.checkout.CheckoutCartRequest;
import com.chitova.florist.model.checkout.UpdateCartRequest;
import com.chitova.florist.model.checkout.UpdateCartResponse;
import com.chitova.florist.outbound.checkout.ElasticPathCloudCheckoutClient;

import com.chitova.florist.outbound.checkout.model.*;
import com.chitova.florist.repositories.CartRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public UpdateCartResponse updateCart(final UpdateCartRequest updateCartRequest) {
        if (Objects.isNull(updateCartRequest.getCartId())) {
            final Cart cart = Cart.builder()
                    .userId(identityUserService.getIdentityUser().sub())
                    .cartItems(updateCartRequest.getCartItems().stream()
                            .map(cartItem -> CartItem.builder()
                                .quantity(cartItem.getQuantity())
                                .sku(cartItem.getSku())
                                .build())
                            .collect(Collectors.toList()))
                    .build();

            final Cart updatedCart = cartRepository.save(cart);
            return UpdateCartResponse.builder()
                    .cartId(updatedCart.getId().toString())
                    .cartItems(updatedCart.getCartItems()
                            .stream()
                            .map(cartItem -> UpdateCartResponse.CartItem.builder()
                                    .quantity(cartItem.getQuantity())
                                    .sku(cartItem.getSku())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        }

        // Update the existing cart
        final List<CartItem> items = updateCartRequest.getCartItems()
                .stream().map(cartItem -> CartItem.builder()
                    .sku(cartItem.getSku())
                    .quantity(cartItem.getQuantity())
                    .build())
                .collect(Collectors.toList());
        final Cart cart = cartRepository.findById(new ObjectId(updateCartRequest.getCartId()))
                .orElseThrow();

        cart.setCartItems(items);
        final Cart updatedCart = cartRepository.save(cart);
        return UpdateCartResponse.builder()
                .cartId(updatedCart.getId().toString())
                .cartItems(updatedCart.getCartItems()
                        .stream()
                        .map(cartItem -> UpdateCartResponse.CartItem.builder()
                                .quantity(cartItem.getQuantity())
                                .sku(cartItem.getSku())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


    public UpdateCartResponse checkout(final CheckoutCartRequest checkoutCartRequest, final String accountManagementToken) {
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

        return UpdateCartResponse.builder()
                .cartId(cartId)
                .cartItems(bulkAddItemsToCartRequest.getData()
                        .stream()
                        .map(customCartItem -> UpdateCartResponse.CartItem.builder().build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static ElasticPathCloudBulkAddItemsToCartRequest getBulkAddItemsToCartRequest(Cart cart) {
        return ElasticPathCloudBulkAddItemsToCartRequest
                .builder()
                .data(cart.getCartItems().stream()
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
