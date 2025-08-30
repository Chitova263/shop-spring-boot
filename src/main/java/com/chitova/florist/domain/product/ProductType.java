package com.chitova.florist.domain.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

public enum ProductType {
    PRODUCT("product"),
    BUNDLE("bundle");

    @Getter
    @JsonValue
    private final String value;

    ProductType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static Optional<ProductType> from(final String value) {
        for (final ProductType productType : ProductType.values()){
            if(Objects.equals(productType.value, value)){
                return Optional.of(productType);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return value;
    }
}
