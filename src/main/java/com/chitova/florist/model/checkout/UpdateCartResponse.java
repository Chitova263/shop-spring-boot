package com.chitova.florist.model.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UpdateCartResponse {
    @Schema(description = "Cart identifier")
    private String cartId;

    @Schema(description = "Cart items")
    private List<UpdateCartResponse.CartItem> cartItems;

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
