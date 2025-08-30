package com.chitova.florist.controller;

import com.chitova.florist.model.checkout.request.CheckoutCartRequest;
import com.chitova.florist.model.checkout.request.UpdateCartRequest;
import com.chitova.florist.model.checkout.response.CartResponse;
import com.chitova.florist.services.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart", description = "Operations interact with cart")
@RestController
public class CartController {
    private final CheckoutService checkoutService;

    public CartController(final CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Operation(summary = "Checkout cart", description = "Checkout cart")
    @PostMapping(value = "/carts/checkout", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public CartResponse checkout(@RequestBody final CheckoutCartRequest request,
                                                 @Parameter(description = "Account Management Token", required = true)
                                      @RequestHeader("EPCC-Account-Management-Token") final String accountManagementToken
    ) {
        return checkoutService.checkout(request, accountManagementToken);
    }

    @Operation(summary = "Update cart", description = "Update cart")
    @PostMapping(value = "/carts", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public CartResponse updateCart(@RequestBody final UpdateCartRequest request) {
        return checkoutService.updateCart(request);
    }

    @Operation(summary = "Add item to cart", description = "AddItem to cart")
    @PostMapping(value = "/carts/add", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public CartResponse addItemToCart(@RequestBody final UpdateCartRequest request) {
        return checkoutService.addItemToCart(request);
    }
}
