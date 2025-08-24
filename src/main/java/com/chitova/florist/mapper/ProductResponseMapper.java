package com.chitova.florist.mapper;

import com.chitova.florist.entities.product.Category;
import com.chitova.florist.entities.product.ChildProduct;
import com.chitova.florist.entities.product.Product;
import com.chitova.florist.entities.product.Variation;
import com.chitova.florist.model.product.GetProductsResponse;

import java.util.*;
import java.util.stream.Collectors;

public class ProductResponseMapper {

    public static List<GetProductsResponse.Category> getProductResponseCategories(final List<Category> categories,
                                                                                  final Map<String, ArrayList<Product>> categoryToProducts) {
        return categories.stream()
                .map(category -> ProductResponseMapper.mapCategory(category, categoryToProducts))
                .collect(Collectors.toList());
    }

    private static GetProductsResponse.Category mapCategory(final Category category, final Map<String, ArrayList<Product>> categoryToProducts) {
        return GetProductsResponse.Category.builder()
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .products(ProductResponseMapper.getProductsOfCategory(category, categoryToProducts))
                .subCategories(category.getSubcategories()
                        .stream()
                        .map(subCategory -> ProductResponseMapper.mapCategory(subCategory, categoryToProducts))
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<GetProductsResponse.Product> getProductsOfCategory(final Category category, final Map<String, ArrayList<Product>> categoryToProducts) {
        return Optional.ofNullable(categoryToProducts.get(category.getName())).orElse(new ArrayList<>())
                .stream()
                .map(ProductResponseMapper::mapProduct)
                .collect(Collectors.toList());
    }

    private static GetProductsResponse.Product mapProduct(final Product product) {
        return GetProductsResponse.Product.builder()
                .sku(product.getSku())
                .name(product.getName())
                .additionalInformation(product.getAdditionalInformation())
                .information(product.getInformation())
                .bestseller(product.isBestseller())
                .variants(product.getChildProducts()
                        .stream()
                        .map(ProductResponseMapper::mapVariants)
                        .collect(Collectors.toList()))
                .build();
    }

    private static GetProductsResponse.Variant mapVariants(final ChildProduct childProduct) {
        return GetProductsResponse.Variant.builder()
                .sku(childProduct.getSku())
                .name(childProduct.getName())
                .additionalInformation(childProduct.getAdditionalInformation())
                .information(childProduct.getInformation())
                .bestseller(childProduct.isBestseller())
                .variations(childProduct.getVariations().stream()
                        .map(ProductResponseMapper::mapVariations)
                        .collect(Collectors.toList()))
                .build();
    }

    private static GetProductsResponse.Variation mapVariations(final Variation variation) {
        return GetProductsResponse.Variation.builder()
                .name(variation.getName())
                .option(GetProductsResponse.VariationOption.builder()
                        .name(variation.getVariationOption().getName())
                        .description(variation.getVariationOption().getDescription())
                        .sortOrder(variation.getVariationOption().getSortOrder())
                        .build())
                .build();
    }
}