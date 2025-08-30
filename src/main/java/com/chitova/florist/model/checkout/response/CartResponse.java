package com.chitova.florist.model.checkout.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CartResponse {
    @Schema(description = "Cart identifier")
    private String cartId;

    @Schema(description = "Cart items")
    private List<CartResponse.CartItem> cartItems;

    @Builder
    @Data
    public static class CartItem {
        @NotNull
        @Schema(description = "Product SKU code")
        private String sku;
        @Schema(description = "Number of items")
        private int quantity;
    }
}
