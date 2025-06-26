package com.chitova.florist.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetProductsResponse {

    @Schema(description = "All the product categories")
    private List<Category> categories;

    @Data
    @Builder
    public static class Category {
        @Schema(description = "Name of the product category", required = true)
        private String name;

        @Schema(description = "Description of the category")
        private String description;

        @Schema(description = "List of subcategories", required = true)
        private List<Category> subCategories;

        @Schema(description = "List of products in the category", required = true)
        private List<Product> products;
    }

    @Data
    @Builder
    public static class Product {
        @Schema(description = "Product SKU code", required = true)
        private String sku;


        @Schema(description = "Name of the product", required = true)
        private String name;


        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @Schema(description = "True if best seller", required = true)
        private boolean bestseller;

        @Schema(description = "List of product variants if parent product", required = true)
        private List<Variant> variants;
    }

    @Data
    @Builder
    public static class Variant {
        @Schema(description = "Product SKU code", required = true)
        private String sku;

        @Schema(description = "Name of the product", required = true)
        private String name;

        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @Schema(description = "True if best seller", required = true)
        private boolean bestseller;

        @Schema(description = "The variations of product", required = true)
        private List<Variation> variations;
    }

    @Data
    @Builder
    public static class Variation {
        @Schema(description = "The variation name", required = true, example = "Size")
        private String name;

        @Schema(description = "The variation option", required = true)
        private VariationOption option;
    }

    @Data
    @Builder
    public static class VariationOption {
        @Schema(description = "The variation option name", required = true, example = "Large")
        private String name;

        @Schema(description = "The variation option description", required = true)
        private String description;

        @Schema(description = "The variation option sorting order")
        private int sortOrder;
    }
}

