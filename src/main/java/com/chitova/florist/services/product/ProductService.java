package com.chitova.florist.services.product;

import com.chitova.florist.common.AsyncUtil;
import com.chitova.florist.domain.price.Price;
import com.chitova.florist.domain.product.Category;
import com.chitova.florist.domain.product.Product;
import com.chitova.florist.mapper.ProductResponseMapper;
import com.chitova.florist.model.product.GetProductsResponse;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.PriceRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceRepository priceRepository;
    private final ProductResponseMapper productResponseMapper;

    public ProductService(final ProductRepository productRepository,
                          final CategoryRepository categoryRepository,
                          final PriceRepository priceRepository,
                          final ProductResponseMapper productResponseMapper
                          ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
        this.productResponseMapper = productResponseMapper;
    }

    public GetProductsResponse getProducts() {
        final CompletableFuture<List<Category>> categories = CompletableFuture.supplyAsync(categoryRepository::findAll);
        final CompletableFuture<List<Product>> products = CompletableFuture.supplyAsync(productRepository::findAll);
        final CompletableFuture<List<Price>> prices = CompletableFuture.supplyAsync(priceRepository::findAll);
        CompletableFuture.allOf(categories, products, prices).join();
        return productResponseMapper.mapProductsResponse(AsyncUtil.getAsync(products), AsyncUtil.getAsync(categories), AsyncUtil.getAsync(prices));
    }
}
