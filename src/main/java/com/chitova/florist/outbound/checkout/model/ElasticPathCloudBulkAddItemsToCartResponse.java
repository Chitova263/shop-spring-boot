package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCloudBulkAddItemsToCartResponse {
    private List<CartItem> data;
    private List<ErrorItem> errors;
    private Meta meta;

    @Data
    @Builder
    public static class CartItem {
        private String id;
        private String type;
        private String name;
        private String description;
        private String sku;
        private String slug;
        private Image image;

        private Integer quantity;

        @JsonProperty("manage_stock")
        private Boolean manageStock;

        @JsonProperty("unit_price")
        private Price unitPrice;

        private Price value;

        private Links links;
        private ItemMeta meta;

        @JsonProperty("product_id")
        private String productId;

        @JsonProperty("catalog_id")
        private String catalogId;

        @JsonProperty("catalog_source")
        private String catalogSource;
    }

    @Data
    @Builder
    public static class Image {
        @JsonProperty("mime_type")
        private String mimeType;

        @JsonProperty("file_name")
        private String fileName;

        private String href;
    }

    @Data
    @Builder
    public static class Price {
        private int amount;
        private String currency;

        @JsonProperty("includes_tax")
        private boolean includesTax;
    }

    @Data
    @Builder
    public static class Links {
        private String product;
    }

    @Data
    @Builder
    public static class ItemMeta {
        @JsonProperty("display_price")
        private DisplayPrice displayPrice;

        private Timestamps timestamps;
    }

    @Data
    @Builder
    public static class DisplayPrice {
        @JsonProperty("with_tax")
        private PriceBreakdown withTax;

        @JsonProperty("without_tax")
        private PriceBreakdown withoutTax;

        private PriceBreakdown tax;
        private PriceBreakdown discount;

        @JsonProperty("without_discount")
        private PriceBreakdown withoutDiscount;

        private PriceBreakdown shipping;

        @JsonProperty("shipping_discount")
        private PriceBreakdown shippingDiscount;
    }

    @Data
    @Builder
    public static class PriceBreakdown {
        private UnitValue unit;
        private UnitValue value;
    }

    @Data
    @Builder
    public static class UnitValue {
        private int amount;
        private String currency;
        private String formatted;
    }

    @Data
    @Builder
    public static class Timestamps {
        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("expires_at")
        private String expiresAt;
    }

    @Data
    @Builder
    public static class ErrorItem {
        private int status;
        private String title;
        private String detail;
        private ErrorMeta meta;
    }

    @Data
    @Builder
    public static class ErrorMeta {
        private String id;
        private String sku;
        private String code;
    }

    @Data
    @Builder
    public static class Meta {
        @JsonProperty("display_price")
        private SummaryDisplayPrice displayPrice;

        private Timestamps timestamps;
    }

    @Data
    @Builder
    public static class SummaryDisplayPrice {
        @JsonProperty("with_tax")
        private UnitValue withTax;

        @JsonProperty("without_tax")
        private UnitValue withoutTax;

        private UnitValue tax;
        private UnitValue discount;

        @JsonProperty("without_discount")
        private UnitValue withoutDiscount;

        private UnitValue shipping;

        @JsonProperty("shipping_discount")
        private UnitValue shippingDiscount;
    }
}
