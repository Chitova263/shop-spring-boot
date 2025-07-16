package com.chitova.florist.model.product;

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
        @Schema(description = "Name of the product category", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;

        @Schema(description = "Description of the category")
        private String description;

        @Schema(description = "List of subcategories", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<Category> subCategories;

        @Schema(description = "List of products in the category", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<Product> products;
    }

    @Data
    @Builder
    public static class Product {
        @Schema(description = "Product SKU code", requiredMode = Schema.RequiredMode.REQUIRED)
        private String sku;

        @Schema(description = "Name of the product", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;

        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @Schema(description = "True if best seller", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean bestseller;

        @Schema(description = "List of product variants if parent product", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<Variant> variants;
    }

    @Data
    @Builder
    public static class Variant {
        @Schema(description = "Product SKU code", requiredMode = Schema.RequiredMode.REQUIRED)
        private String sku;

        @Schema(description = "Name of the product", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;

        @Schema(description = "Information about product")
        private String information;

        @Schema(description = "Additional information about product")
        private String additionalInformation;

        @Schema(description = "True if best seller", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean bestseller;

        @Schema(description = "The variations of product", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<Variation> variations;
    }

    @Data
    @Builder
    public static class Variation {
        @Schema(description = "The variation name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Size")
        private String name;

        @Schema(description = "The variation option", requiredMode = Schema.RequiredMode.REQUIRED)
        private VariationOption option;
    }

    @Data
    @Builder
    public static class VariationOption {
        @Schema(description = "The variation option name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Large")
        private String name;

        @Schema(description = "The variation option description", requiredMode = Schema.RequiredMode.REQUIRED)
        private String description;

        @Schema(description = "The variation option sorting order")
        private int sortOrder;
    }
}

