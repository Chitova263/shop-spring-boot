package com.chitova.florist.model.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GetProductsResponse {

    @Schema(description = "All the product categories")
    private List<Category> categories;

    @Data
    @Builder
    public static class Category {
        @NotNull
        @Schema(description = "Name of the product category")
        private String name;

        @Schema(description = "Description of the category")
        private String description;

        @Schema(description = "Slug")
        private String slug;

        @NotNull
        @Schema(description = "List of subcategories")
        private List<Category> subCategories;

        @NotNull
        @Schema(description = "List of products in the category")
        private List<Product> products;
    }

    @Data
    @Builder
    public static class Product {
        @NotNull
        @Schema(description = "Product SKU code")
        private String sku;

        @NotNull
        @Schema(description = "Name of the product")
        private String name;

        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @NotNull
        @Schema(description = "True if best seller")
        private boolean bestseller;

        @Schema(description = "Price of the product")
        private double price;

        @Schema(description = "List price of the product")
        private double listPrice;

        @NotNull
        @Schema(description = "List of product variants if parent product")
        private List<Variant> variants;
    }

    @Data
    @Builder
    public static class Variant {
        @NotNull
        @Schema(description = "Product SKU code")
        private String sku;

        @NotNull
        @Schema(description = "Name of the product")
        private String name;

        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @NotNull
        @Schema(description = "True if best seller")
        private boolean bestseller;

        @Schema(description = "Price of the product")
        private double price;

        @Schema(description = "List price of the product")
        private double listPrice;

        @NotNull
        @Schema(description = "The variations of product")
        private List<Variation> variations;
    }

    @Data
    @Builder
    public static class Variation {
        @NotNull
        @Schema(description = "The variation name", example = "Size")
        private String name;

        @NotNull
        @Schema(description = "The variation option")
        private VariationOption option;
    }

    @Data
    @Builder
    public static class VariationOption {
        @NotNull
        @Schema(description = "The variation option name", example = "Large")
        private String name;

        @NotNull
        @Schema(description = "The variation option description")
        private String description;

        @Schema(description = "The variation option sorting order")
        private int sortOrder;
    }
}

