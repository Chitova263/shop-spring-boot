package com.chitova.florist.outbound.products;

import com.chitova.florist.outbound.products.response.ElasticPathProductsResponse;

import java.util.*;


public final class ElasticPathProductResponseAccessor {

    private static final Set<String> PARENT_OR_STANDARD_PRODUCT_TYPES = Set.of("parent", "standard");
    private static final Set<String> CHILD_PRODUCT_TYPES = Set.of("child");

    public static String getInformation(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product)
                .map(ElasticPathProductsResponse.Product::getAttributes)
                .map(ElasticPathProductsResponse.ProductAttributes::getExtensions)
                .map(ElasticPathProductsResponse.Extensions::getProductFlower)
                .map(ElasticPathProductsResponse.ProductFlower::getInformation)
                .orElse(null);
    }

    public static String getAdditionalInformation(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product)
                .map(ElasticPathProductsResponse.Product::getAttributes)
                .map(ElasticPathProductsResponse.ProductAttributes::getExtensions)
                .map(ElasticPathProductsResponse.Extensions::getProductFlower)
                .map(ElasticPathProductsResponse.ProductFlower::getAdditionalInformation)
                .orElse(null);
    }

    public static String getElasticPathCloudParentId(final ElasticPathProductsResponse.Product product) {
        if (product.getRelationships() == null) {
            throw new IllegalArgumentException("relationships is null");
        }
        if (product.getRelationships().get("base_product") instanceof Map<?, ?> baseProduct
                && baseProduct.get("data") instanceof Map<?, ?> obj
                && obj.get("id") != null) {
            return obj.get("id").toString();
        }

        return null;
    }

    public static List<ElasticPathProductsResponse.ChildVariation> getChildVariations(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product.getMeta().getChildVariations()).orElse(new ArrayList<>());
    }

    public static boolean isParentOrStandardProduct(final ElasticPathProductsResponse.Product product) {
        return product.getMeta().getProductTypes().stream()
                .map(String::toLowerCase)
                .anyMatch(PARENT_OR_STANDARD_PRODUCT_TYPES::contains);
    }

    public static boolean isChildProduct(final ElasticPathProductsResponse.Product product) {
        return product.getMeta().getProductTypes().stream()
                .map(String::toLowerCase)
                .anyMatch(CHILD_PRODUCT_TYPES::contains);
    }
}
