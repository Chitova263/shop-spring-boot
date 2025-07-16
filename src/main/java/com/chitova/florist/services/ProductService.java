package com.chitova.florist.services;

import com.chitova.florist.entities.product.Category;
import com.chitova.florist.entities.product.Product;
import com.chitova.florist.mapper.ProductResponseMapper;
import com.chitova.florist.model.product.GetProductsResponse;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(final ProductRepository productRepository,
                          final CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public GetProductsResponse getProducts() {
        final List<Category> categories = categoryRepository.findAll();
        final List<Product> products = productRepository.findAll();
        return ProductService.getGetProductsResponse(products, categories);
    }

    private static GetProductsResponse getGetProductsResponse(List<Product> products, List<Category> categories) {
        final Map<String, ArrayList<Product>> categoryToProducts = new HashMap<>();
        for (final Product product : products) {
            for (final Category category : product.getCategories()) {
                categoryToProducts
                        .computeIfAbsent(category.getName(), k -> new ArrayList<>())
                        .add(product);
            }
        }
        return GetProductsResponse.builder()
                .categories(ProductResponseMapper.getProductResponseCategories(categories, categoryToProducts))
                .build();
    }
}
