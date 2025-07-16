package com.chitova.florist.outbound.products;

import com.chitova.florist.outbound.products.models.ElasticPathCloudProductsResponse;

import java.util.*;

public class ElasticPathCloudMultiProductsResponseAccessor {

    public static Optional<String> getInformation(ElasticPathCloudProductsResponse.Product product) {
        if (Objects.isNull(product.getAttributes().getExtensions())) {
            return Optional.empty();
        }
        return Optional.of(product.getAttributes().getExtensions().getProductFlower().getInformation());
    }

    public static Optional<String> getAdditionalInformation(ElasticPathCloudProductsResponse.Product product) {
        if (Objects.isNull(product.getAttributes().getExtensions())) {
            return Optional.empty();
        }
        return Optional.of(product.getAttributes().getExtensions().getProductFlower().getAdditionalInformation());
    }

    public static boolean isParentProduct(ElasticPathCloudProductsResponse.Product data) {
        return data.getMeta()
                .getProductTypes()
                .stream()
                .anyMatch(productType -> productType.compareToIgnoreCase("parent") == 0);
    }

    public static String getElasticPathCloudParentId(Map<String, Object> relationships) {
        if(Objects.isNull(relationships)) {
            throw new IllegalArgumentException("relationships is null");
        }
        final Map<String, Object>  baseProduct = (Map<String, Object>)relationships.get("base_product");
        final Map<String, Object> data =  (Map<String, Object> )baseProduct.get("data");
        return data.get("id").toString();
    }
}
