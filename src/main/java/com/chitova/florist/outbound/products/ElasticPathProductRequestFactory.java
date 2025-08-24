package com.chitova.florist.outbound.products;

import com.chitova.florist.outbound.products.request.ElasticPathCreateNodeRelationshipToProductsRequest;
import com.chitova.florist.outbound.products.request.ElasticPathCreateProductRequest;
import com.chitova.florist.services.MockProduct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ElasticPathProductRequestFactory {

    public static ElasticPathCreateNodeRelationshipToProductsRequest createNodeRelationshipToProductsRequest(final List<MockProduct> mockProducts, Map<String, String> createdProductSkuToElasticPathProductId) {
        final var dataNodes = mockProducts.stream()
                .map(mockProduct -> ElasticPathCreateNodeRelationshipToProductsRequest.DataNode.builder()
                        .type("product")
                        .id(Optional.ofNullable(createdProductSkuToElasticPathProductId.get(mockProduct.getData().getAttributes().getSku()))
                                .orElseThrow(() -> new RuntimeException(
                                        "Product not found for SKU: " + mockProduct.getData().getAttributes().getSku())))
                        .build())
                .collect(Collectors.toList());


        return ElasticPathCreateNodeRelationshipToProductsRequest.builder().data(dataNodes).build();
    }

    public static ElasticPathCreateProductRequest createElasticPathCloudCreateProductRequest(final MockProduct mockProduct) {
        final var attributes = mockProduct.getData().getAttributes();
        return ElasticPathCreateProductRequest.builder()
                .data(ElasticPathCreateProductRequest.DataWrapper.builder()
                        .type("product")
                        .attributes(ElasticPathCreateProductRequest.DataWrapper.Attributes.builder()
                                .slug(attributes.getSlug())
                                .name(attributes.getName())
                                .description(attributes.getDescription())
                                .status(attributes.getStatus())
                                .commodityType(attributes.getCommodityType())
                                .mpn(attributes.getMpn())
                                .sku(attributes.getSku())
                                .upcEan("9081726354")
                                .build())
                        .build())
                .build();
    }
}
