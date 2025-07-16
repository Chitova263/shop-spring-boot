package com.chitova.florist.controller;

import com.chitova.florist.model.checkout.UpdateCartRequest;
import com.chitova.florist.model.checkout.UpdateCartResponse;
import com.chitova.florist.services.CheckoutService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Checkout")
@RequestMapping("/florist-api")
@RestController
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(final CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout/carts")
    public ResponseEntity<?> updateCart(@RequestBody final UpdateCartRequest request) {
        final UpdateCartResponse response = checkoutService.updateCart(request);
        return ResponseEntity.ok(response);
    }
}
