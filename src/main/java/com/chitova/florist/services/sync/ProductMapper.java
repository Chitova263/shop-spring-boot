package com.chitova.florist.services.sync;

import com.chitova.florist.entities.product.*;
import com.chitova.florist.outbound.products.ElasticPathProductResponseAccessor;
import com.chitova.florist.outbound.products.response.ElasticPathProductsResponse;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

public final class ProductMapper {
    public static ChildProduct toChildProduct(final ElasticPathProductsResponse.Product product) {
        return ChildProduct.builder()
                .elasticPathCloudProductId(product.getId())
                .elasticPathCloudParentProductId(ElasticPathProductResponseAccessor.getElasticPathCloudParentId(product))
                .sku(product.getAttributes().getSku())
                .name(product.getAttributes().getName())
                .bestseller(false)
                .information(ElasticPathProductResponseAccessor.getInformation(product))
                .additionalInformation(ElasticPathProductResponseAccessor.getAdditionalInformation(product))
                .variations(ProductMapper.getVariations(product))
                .build();
    }

    private static Set<Variation> getVariations(ElasticPathProductsResponse.Product product) {
        return ElasticPathProductResponseAccessor.getChildVariations(product)
                .stream()
                .map(ProductMapper::toVariation)
                .collect(Collectors.toSet());
    }

    private static Variation toVariation(final ElasticPathProductsResponse.ChildVariation childVariation) {
        if (Objects.isNull(childVariation)) {
            return null;
        }
        return Variation.builder()
                .elasticPathCloudVariationId(childVariation.getId())
                .name(childVariation.getName())
                .variationOption(VariationOption.builder()
                        .elasticPathCloudVariationOptionId(childVariation.getSingleOption().getId())
                        .name(childVariation.getSingleOption().getName())
                        .description(childVariation.getSingleOption().getDescription())
                        .sortOrder(childVariation.getSortOrder())
                        .build())
                .build();
    }

    public static Product toProduct(final ElasticPathProductsResponse.Product product,
                                    final List<ChildProduct> childProducts,
                                    final HashMap<String, ArrayList<Category>> productToCategoryMap) {
        return Product.builder()
                .id(new ObjectId())
                .sku(product.getAttributes().getSku())
                .name(product.getAttributes().getName())
                .bestseller(false)
                .information(ElasticPathProductResponseAccessor.getInformation(product))
                .additionalInformation(ElasticPathProductResponseAccessor.getAdditionalInformation(product))
                .elasticPathCloudProductId(product.getId())
                .childProducts(ProductMapper.getChildProducts(product, childProducts))
                .categories(ProductMapper.getCategories(product, productToCategoryMap))
                .build();
    }

    private static HashSet<Category> getCategories(final ElasticPathProductsResponse.Product product,
                                                   final HashMap<String, ArrayList<Category>> productToCategoryMap) {
        return new HashSet<>(productToCategoryMap.get(product.getAttributes().getSku()));
    }

    private static Set<ChildProduct> getChildProducts(final ElasticPathProductsResponse.Product product,
                                                      final List<ChildProduct> childProducts) {
        return childProducts.stream()
                .filter(childProduct -> childProduct.getElasticPathCloudParentProductId().equals(product.getId()))
                .collect(Collectors.toSet());
    }
}
