package com.chitova.florist.outbound.products;

import com.chitova.florist.domain.product.ProductType;
import com.chitova.florist.outbound.products.response.ElasticPathProductsResponse;

import java.util.*;


public final class ElasticPathProductResponseAccessor {

    private static final Set<String> PARENT_OR_STANDARD_PRODUCT_TYPES = Set.of("parent", "standard");
    private static final Set<String> CHILD_PRODUCT_TYPES = Set.of("child");

    public static String getInformation(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product)
                .map(ElasticPathProductsResponse.Product::attributes)
                .map(ElasticPathProductsResponse.ProductAttributes::extensions)
                .map(ElasticPathProductsResponse.Extensions::productFlower)
                .map(ElasticPathProductsResponse.ProductFlower::information)
                .orElse(null);
    }

    public static String getAdditionalInformation(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product)
                .map(ElasticPathProductsResponse.Product::attributes)
                .map(ElasticPathProductsResponse.ProductAttributes::extensions)
                .map(ElasticPathProductsResponse.Extensions::productFlower)
                .map(ElasticPathProductsResponse.ProductFlower::additionalInformation)
                .orElse(null);
    }

    public static String getElasticPathCloudParentProductId(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product)
                .map(ElasticPathProductsResponse.Product::relationships)
                .map(ElasticPathProductsResponse.Relationships::baseProduct)
                .map(ElasticPathProductsResponse.Relationship::data)
                .map(ElasticPathProductsResponse.RelationshipData::id)
                .orElse(null);
    }


    public static ProductType getProductType(ElasticPathProductsResponse.Product product) {
        return ProductType.from(product.type()).orElseThrow();
    }


    public static List<ElasticPathProductsResponse.ChildVariation> getChildVariations(final ElasticPathProductsResponse.Product product) {
        return Optional.ofNullable(product.meta().childVariations()).orElse(Collections.emptyList());
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
