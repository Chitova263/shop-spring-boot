package com.chitova.florist.controller;

import com.chitova.florist.model.product.GetProductsResponse;
import com.chitova.florist.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Products")
@RestController
public class ProductsController {
    private final ProductService productService;

    public ProductsController(final ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products in the catalog", description = "Returns the product catalog")
    @GetMapping("products")
    public GetProductsResponse getProducts() {
        return productService.getProducts();
    }
}
