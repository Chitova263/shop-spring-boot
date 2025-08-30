package com.chitova.florist.mapper;

import com.chitova.florist.domain.price.Price;
import com.chitova.florist.domain.product.Category;
import com.chitova.florist.domain.product.Product;
import com.chitova.florist.domain.product.Variation;
import com.chitova.florist.model.product.GetProductsResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductResponseMapper {

    public static final String CURRENCY_USD = "USD";

    public GetProductsResponse mapProductsResponse(final List<Product> products,
                                                   final List<Category> categories,
                                                   final List<Price> prices) {
        final Map<String, ArrayList<Product>> categoryToProducts = new HashMap<>();
        for (final Product product : products) {
            for (final Category category : product.getElasticPathCloudCategoryIds()) {
                categoryToProducts
                        .computeIfAbsent(category.getName(), k -> new ArrayList<>())
                        .add(product);
            }
        }
        final Map<String, Price> skuToPrices = prices.stream().collect(Collectors.toMap(Price::getSku, price -> price));
        return GetProductsResponse.builder()
                .categories(mapCategoriesResponse(categories, categoryToProducts, skuToPrices))
                .build();
    }

    private List<GetProductsResponse.Category> mapCategoriesResponse(final List<Category> categories,
                                                                     final Map<String, ArrayList<Product>> categoryToProducts,
                                                                     final Map<String, Price> skuToPrices) {
        return categories.stream()
                .map(category -> GetProductsResponse.Category.builder()
                        .name(category.getName())
                        .description(category.getDescription())
                        .slug(category.getSlug())
                        .products(mapCategoryProducts(category, categoryToProducts, skuToPrices))
                        .subCategories(mapCategoriesResponse(category.getSubcategories(), categoryToProducts, skuToPrices))
                        .build())
                .collect(Collectors.toList());
    }


    private List<GetProductsResponse.Product> mapCategoryProducts(final Category category,
                                                                         final Map<String, ArrayList<Product>> categoryToProducts,
                                                                         final Map<String, Price> skuToPrices) {
        return Optional.ofNullable(categoryToProducts.get(category.getName())).orElse(new ArrayList<>())
                .stream()
                .map(product -> mapProduct(product, skuToPrices))
                .collect(Collectors.toList());
    }

    private GetProductsResponse.Product mapProduct(final Product product, final Map<String, Price> skuToPrices) {
        final double price = skuToPrices.get(product.getSku()).getPrice().get(CURRENCY_USD).getAmount();
        return GetProductsResponse.Product.builder()
                .sku(product.getSku())
                .name(product.getName())
                .additionalInformation(product.getAdditionalInformation())
                .information(product.getInformation())
                .bestseller(product.isBestseller())
                .price(price)
                .listPrice(price)
                .variants(product.getProductVariants()
                        .stream()
                        .map(variant -> mapVariants(variant, skuToPrices))
                        .collect(Collectors.toList()))
                .build();
    }

    private GetProductsResponse.Variant mapVariants(final ProductVariant productVariant,
                                                           final Map<String, Price> skuToPrices) {
        final double price = skuToPrices.get(productVariant.getSku()).getPrice().get(CURRENCY_USD).getAmount();

        return GetProductsResponse.Variant.builder()
                .sku(productVariant.getSku())
                .name(productVariant.getName())
                .additionalInformation(productVariant.getAdditionalInformation())
                .information(productVariant.getInformation())
                .bestseller(productVariant.isBestseller())
                .price(price)
                .listPrice(price)
                .variations(productVariant.getVariations().stream()
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