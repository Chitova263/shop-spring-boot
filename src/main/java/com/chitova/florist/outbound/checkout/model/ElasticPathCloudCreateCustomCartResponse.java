package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudCreateCustomCartResponse {
    private CartData data;

    @Data
    public static class CartData {
        private String id;
        private String type;
        private String name;
        private String description;
        private Contact contact;

        @JsonProperty("discount_settings")
        private DiscountSettings discountSettings;

        @JsonProperty("inventory_settings")
        private InventorySettings inventorySettings;

        private Links links;
        private Meta meta;
        private Relationships relationships;
    }

    @Data
    public static class Contact {
        private String email;
    }

    @Data
    public static class DiscountSettings {
        @JsonProperty("custom_discounts_enabled")
        private boolean customDiscountsEnabled;
    }

    @Data
    public static class InventorySettings {
        @JsonProperty("defer_inventory_check")
        private boolean deferInventoryCheck;
    }

    @Data
    public static class Links {
        private String self;
    }

    @Data
    public static class Meta {
        @JsonProperty("display_price")
        private DisplayPrice displayPrice;
        private Timestamps timestamps;
    }

    @Data
    public static class DisplayPrice {
        @JsonProperty("with_tax")
        private Price withTax;

        @JsonProperty("without_tax")
        private Price withoutTax;

        private Price tax;
        private Price discount;

        @JsonProperty("without_discount")
        private Price withoutDiscount;

        private Price shipping;

        @JsonProperty("shipping_discount")
        private Price shippingDiscount;
    }

    @Data
    public static class Price {
        private int amount;
        private String currency;
        private String formatted;
    }

    @Data
    public static class Timestamps {
        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("expires_at")
        private String expiresAt;
    }

    @Data
    public static class Relationships {
        private Items items;
    }

    @Data
    public static class Items {
        private Object data; // Replace with List<ItemData> or appropriate type if known
    }
}
