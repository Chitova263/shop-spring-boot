package com.chitova.florist.model.checkout.request;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCartRequest {
    @Schema(description = "Cart identifier")
    private String cartId;

    @NotNull
    @Schema(description = "Cart items")
    private List<CartLineItem> lineItems;

    @Data
    public static class CartLineItem {
        @NotNull
        @Schema(description = "Product SKU code")
        private String sku;
        @Schema(description = "Number of items")
        private int quantity;
    }
}
