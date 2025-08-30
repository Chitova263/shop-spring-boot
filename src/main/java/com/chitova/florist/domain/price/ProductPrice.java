package com.chitova.florist.domain.price;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Getter
public class ProductPrice {
    @Field(name = "amount")
    private double amount;

    @Field(name = "includes_tax")
    private boolean includesTax;
}
