package com.chitova.florist.domain.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CartItem {
    private String sku;
    private int quantity;
}
