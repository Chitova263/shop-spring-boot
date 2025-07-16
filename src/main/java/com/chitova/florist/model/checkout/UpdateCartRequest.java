package com.chitova.florist.model.checkout;

import io.swagger.v3.oas.annotations.media.*;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCartRequest {
    @Schema(description = "Cart identifier")
    private String cartId;

    @Schema(description = "Items added to the cart")
    private List<CartItem> cartItems;

    @Data
    public static class CartItem {
        @Schema(description = "Product sku", requiredMode = Schema.RequiredMode.REQUIRED)
        private String sku;
        @Schema(description = "Quantity", requiredMode = Schema.RequiredMode.REQUIRED)
        private int quantity;
    }
}
