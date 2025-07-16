package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudCheckoutResponse {
    private OrderData data;
    private Included included;

    @Data
    public static class OrderData {
        private String type;
        private String id;
        private String status;
        private String payment;
        private String shipping;
        private boolean anonymized;
        private Contact contact;

        @JsonProperty("billing_address")
        private Address billingAddress;

        @JsonProperty("shipping_address")
        private Address shippingAddress;

        private Meta meta;
        private Relationships relationships;
    }

    @Data
    public static class Contact {
        private String name;
        private String email;
    }

    @Data
    public static class Address {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("company_name")
        private String companyName;

        private String line_1;
        private String line_2;
        private String city;
        private String postcode;
        private String county;
        private String country;
        private String region;
        private String instructions;
        private String phoneNumber;
    }

    @Data
    public static class Meta {
        @JsonProperty("display_price")
        private DisplayPrice displayPrice;
        private Timestamps timestamps;
    }

    @Data
    public static class DisplayPrice {
        private Price withTax;
        private Price withoutTax;
        private Price tax;
        private Price discount;
        private Price withoutDiscount;
        private Price shipping;
        @JsonProperty("shipping_discount")
        private Price shippingDiscount;
        @JsonProperty("balance_owing")
        private Price balanceOwing;
        private Price paid;
        private Price authorized;
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
    }

    @Data
    public static class Relationships {
        private Items items;
        private RelationshipData account;
        @JsonProperty("account_member")
        private RelationshipData accountMember;
    }

    @Data
    public static class Items {
        private java.util.List<RelationshipData> data;
    }

    @Data
    public static class RelationshipData {
        private String type;
        private String id;
    }

    @Data
    public static class Included {
        private java.util.List<OrderItem> items;
    }

    @Data
    public static class OrderItem {
        private String type;
        private String id;
        private int quantity;

        @JsonProperty("product_id")
        private String productId;

        private String name;
        private String sku;

        @JsonProperty("unit_price")
        private UnitPrice unitPrice;

        private UnitPrice value;
        private OrderItemMeta meta;
        private Relationships relationships;

        @JsonProperty("catalog_id")
        private String catalogId;

        @JsonProperty("catalog_source")
        private String catalogSource;
    }

    @Data
    public static class UnitPrice {
        private int amount;
        private String currency;

        @JsonProperty("includes_tax")
        private boolean includesTax;
    }

    @Data
    public static class OrderItemMeta {
        @JsonProperty("display_price")
        private OrderItemDisplayPrice displayPrice;
        private Timestamps timestamps;
    }

    @Data
    public static class OrderItemDisplayPrice {
        @JsonProperty("with_tax")
        private UnitValue withTax;
        @JsonProperty("without_tax")
        private UnitValue withoutTax;
        private UnitValue tax;
        private UnitValue discount;
        @JsonProperty("without_discount")
        private UnitValue withoutDiscount;
    }

    @Data
    public static class UnitValue {
        private Price unit;
        private Price value;
    }
}
