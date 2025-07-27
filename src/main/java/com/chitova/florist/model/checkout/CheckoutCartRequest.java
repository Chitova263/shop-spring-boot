package com.chitova.florist.model.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutCartRequest {
    @NotNull
    @Schema(description = "Cart identifier")
    private String cartId;
}
