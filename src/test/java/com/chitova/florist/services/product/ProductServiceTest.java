package com.chitova.florist.services.product;

import com.chitova.florist.fixture.ProductResponseFixture;
import com.chitova.florist.mapper.ProductResponseMapper;
import com.chitova.florist.model.product.GetProductsResponse;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.PriceRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private PriceRepository priceRepository;
    private ProductResponseMapper productResponseMapper;

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        priceRepository = mock(PriceRepository.class);
        productResponseMapper = mock(ProductResponseMapper.class);
        productService = new ProductService(productRepository, categoryRepository, priceRepository, productResponseMapper);
    }

    @Test
    public void should_get_all_products() {
        // given
        when(productRepository.findAll()).thenReturn(ProductResponseFixture.getAllProducts());
        when(categoryRepository.findAll()).thenReturn(ProductResponseFixture.getAllCategories());
        // when
        final GetProductsResponse actual = productService.getProducts();
        // then
        assertThat(actual.getCategories().size()).isEqualTo(3);
    }
}
