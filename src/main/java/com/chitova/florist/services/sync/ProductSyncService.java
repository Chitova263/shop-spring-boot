package com.chitova.florist.services.sync;

import com.chitova.florist.common.AsyncUtil;
import com.chitova.florist.domain.price.Price;
import com.chitova.florist.domain.price.ProductPrice;
import com.chitova.florist.domain.pricebook.Pricebook;
import com.chitova.florist.domain.product.*;
import com.chitova.florist.outbound.products.ElasticPathProductExperienceManagerClient;
import com.chitova.florist.outbound.products.ElasticPathProductResponseAccessor;
import com.chitova.florist.outbound.products.response.ElasticPathNodeChildrenResponse;
import com.chitova.florist.outbound.products.response.ElasticPathNodeProductsResponse;
import com.chitova.florist.outbound.products.response.ElasticPathProductPricesResponse;
import com.chitova.florist.outbound.products.response.ElasticPathProductsResponse;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.PriceRepository;
import com.chitova.florist.repositories.PricebookRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProductSyncService {

    private final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PricebookRepository pricebookRepository;
    private final PriceRepository priceRepository;

    public ProductSyncService(final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient,
                              final CategoryRepository categoryRepository,
                              final ProductRepository productRepository,
                              final PricebookRepository pricebookRepository,
                              final PriceRepository priceRepository
                              ) {
        this.elasticPathProductExperienceManagerClient = elasticPathProductExperienceManagerClient;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.pricebookRepository = pricebookRepository;
        this.priceRepository = priceRepository;
    }

    public void updateCatalog() {
        final var categories = getCategories();
        final var elasticPathCategoryIdToElasticPathNodeProductsResponse = getElasticPathCategoryIdToElasticPathNodeProductsResponse(categories, new HashMap<>());
        final var products = getProducts(elasticPathCategoryIdToElasticPathNodeProductsResponse);
    }

    private List<Product> getProducts(final Map<String, ElasticPathNodeProductsResponse> elasticPathCategoryIdToElasticPathNodeProductsResponse) {
        final var elasticPathProductIdToCategory = ProductSyncService.getElasticPathProductIdToCategory(elasticPathCategoryIdToElasticPathNodeProductsResponse);
        final var productsResponse = elasticPathProductExperienceManagerClient.getProducts();
        return productsResponse.data()
                .stream()
                .map(product -> ProductSyncService.mapProduct(product, elasticPathProductIdToCategory))
                .toList();
    }

    private static Map<String, Set<String>> getElasticPathProductIdToCategory(Map<String, ElasticPathNodeProductsResponse> elasticPathCategoryIdToElasticPathNodeProductsResponse) {
        final Map<String, Set<String>> elasticPathProductIdToCategory = new HashMap<>();
        elasticPathCategoryIdToElasticPathNodeProductsResponse.forEach((elasticPathCloudCategoryId, elasticPathNodeProductsResponse) ->
                        elasticPathNodeProductsResponse.getData().forEach(product ->
                            elasticPathProductIdToCategory
                                    .computeIfAbsent(product.getId(), k -> new HashSet<>())
                                    .add(elasticPathCloudCategoryId)
                ));
        return elasticPathProductIdToCategory;
    }

    private static Product mapProduct(
            final ElasticPathProductsResponse.Product product,
            final Map<String, Set<String>> elasticPathProductIdToCategory) {
        final Set<String> elasticPathCloudCategoryIds = Optional.ofNullable(elasticPathProductIdToCategory.get(product.id()))
                .orElse(Collections.emptySet());
        return Product.builder()
                .elasticPathCloudProductId(product.id())
                .slug(product.attributes().slug())
                .productType(ElasticPathProductResponseAccessor.getProductType(product))
                .name(product.attributes().name())
                .description(product.attributes().description())
                .information(ElasticPathProductResponseAccessor.getInformation(product))
                .additionalInformation(ElasticPathProductResponseAccessor.getAdditionalInformation(product))
                .bestseller(true)
                .elasticPathCloudCategoryIds(elasticPathCloudCategoryIds)
                .elasticPathCloudParentProductId(ElasticPathProductResponseAccessor.getElasticPathCloudParentProductId(product))
                .variations(ProductSyncService.getProductVariations(product))
                .build();
    }

    private static Set<Variation> getProductVariations(ElasticPathProductsResponse.Product product) {
        return ElasticPathProductResponseAccessor.getChildVariations(product).stream()
                .map(childVariation -> Variation.builder()
                        .elasticPathCloudVariationId(childVariation.id())
                        .name(childVariation.name())
                        .build())
                .collect(Collectors.toSet());
    }

    private List<Category> getCategories() {
        final var hierarchies = this.elasticPathProductExperienceManagerClient.getHierarchies();
        final var hierarchy = hierarchies.getData().stream()
                .findFirst()
                .orElseThrow();
        final var hierarchyChildNodesResponse = elasticPathProductExperienceManagerClient.getHierarchyChildNodes(hierarchy.getId());
        final var hierarchyChildNodes = AsyncUtil.getAsync(hierarchyChildNodesResponse);
        final var categories = hierarchyChildNodes.getData()
                .stream()
                .map(node -> CategoryMapper.toCategory(node, hierarchy.getId()))
                .collect(Collectors.toList());
        populateSubcategoriesRecursively(categories, hierarchy.getId());
        return categories;
    }

    public void updatePricebooks() {
        final var priceBooksResponse = elasticPathProductExperienceManagerClient.getPricebooks();
        final var pricebooks = priceBooksResponse.getData().stream()
                .map(data -> Pricebook.builder()
                        .elasticPathCloudPricebookId(data.getId())
                        .name(data.getAttributes().getName())
                        .description(data.getAttributes().getDescription())
                        .updatedAt(Instant.parse(data.getAttributes().getUpdatedAt()))
                        .build())
                .toList();
        pricebookRepository.deleteAll();
        pricebookRepository.saveAll(pricebooks);
    }

    public void updateProductPrices() {
        final var pricebooks = pricebookRepository.findAll();
        final var pricebook = pricebooks.stream().findFirst().orElseThrow();

        final ElasticPathProductPricesResponse productPricesResponse = elasticPathProductExperienceManagerClient
                .getProductPrices(pricebook.getElasticPathCloudPricebookId());

        final List<Price> prices = productPricesResponse.getData().stream()
                .map(data -> Price.builder()
                        .sku(data.getAttributes().getSku())
                        .elasticPathCloudPriceId(data.getId())
                        .createdAt(Instant.parse(data.getAttributes().getCreatedAt()))
                        .updatedAt(Instant.parse(data.getAttributes().getUpdatedAt()))
                        .price(Optional.ofNullable(data.getAttributes().getCurrencies()).orElse(new HashMap<>()).entrySet()
                                .stream()
                                .map(entry -> new CurrencyPrice(entry.getKey(), entry.getValue()))
                                .collect(Collectors.toMap(CurrencyPrice::currency, currencyPrice -> ProductPrice.builder()
                                        .amount(currencyPrice.value().getAmount())
                                        .includesTax(currencyPrice.value().isIncludesTax())
                                        .build()))
                        )
                        .build())
                .toList();
        priceRepository.deleteAll();
        priceRepository.saveAll(prices);
    }

    private void populateSubcategoriesRecursively(final List<Category> categories,
                                                  final String hierarchyId) {
        if(categories.isEmpty()){
            return;
        }
        final var parentCategoriesHavingSubcategories = categories.stream()
                .filter(Category::isHasSubcategories)
                .toList();

        final var parentElasticPathCloudCategoryIdToNodeChildrenResponsesFutures = parentCategoriesHavingSubcategories
                        .stream()
                        .collect(Collectors.toMap(Category::getElasticPathCloudCategoryId, this::getNodeChildren));
        // Wait for all the responses
        CompletableFuture.allOf(parentElasticPathCloudCategoryIdToNodeChildrenResponsesFutures.values().toArray(CompletableFuture[]::new));

        final var parentElasticPathCloudCategoryIdToSubcategories = parentElasticPathCloudCategoryIdToNodeChildrenResponsesFutures.entrySet()
                .stream()
                .map(entry -> {
                    final var elasticPathCloudParentCategoryId = entry.getKey();
                    final var nodeChildrenResponse = entry.getValue().join();
                    final var subcategories = CategoryMapper.mapNodeDataToCategories(hierarchyId, nodeChildrenResponse);
                    return new ParentCategoryWithSubcategories(elasticPathCloudParentCategoryId, subcategories);
                })
                .collect(Collectors.toMap(ParentCategoryWithSubcategories::parentElasticPathCloudCategoryId, ParentCategoryWithSubcategories::subcategories));

        parentCategoriesHavingSubcategories.forEach(parentCategory -> {
            final var subcategories = parentElasticPathCloudCategoryIdToSubcategories.get(parentCategory.getElasticPathCloudCategoryId());
            parentCategory.getSubcategories().addAll(subcategories);
        });
        final var allSubcategories = parentCategoriesHavingSubcategories.stream()
                .map(Category::getSubcategories)
                .flatMap(List::stream)
                .toList();
        populateSubcategoriesRecursively(allSubcategories, hierarchyId);
    }

    private CompletableFuture<ElasticPathNodeChildrenResponse> getNodeChildren(Category category) {
        return elasticPathProductExperienceManagerClient.getNodeChildrenResponse(
                category.getElasticPathCloudHierarchyId(),
                category.getElasticPathCloudCategoryId());
    }

    private Map<String, ElasticPathNodeProductsResponse> getElasticPathCategoryIdToElasticPathNodeProductsResponse(
            final List<Category> categories,
            final Map<String, ElasticPathNodeProductsResponse> categoryIdToNodeProductsResponses) {
        if(categories.isEmpty()){
            return categoryIdToNodeProductsResponses;
        }
        final var elasticPathCloudCategoryIdToNodeProductsResponsesFutures = categories.stream()
                .collect(Collectors.toMap(Category::getElasticPathCloudCategoryId, this::getNodeProducts));

        CompletableFuture.allOf(elasticPathCloudCategoryIdToNodeProductsResponsesFutures.values().toArray(CompletableFuture[]::new));

        var elasticPathCloudCategoryIdToNodeProductsResponses = elasticPathCloudCategoryIdToNodeProductsResponsesFutures.entrySet()
                .stream()
                .map(entry -> {
                    final var productsResponse = entry.getValue().join();
                    final var categoryId = entry.getKey();
                    return new CategoryWithProducts(categoryId, productsResponse);
                })
                .collect(Collectors.toMap(CategoryWithProducts::elasticPathCloudCategoryId, CategoryWithProducts::elasticPathNodeProductsResponses));
        // recursively fetch products for subcategories
        var subcategories = categories.stream().flatMap(category -> category.getSubcategories().stream()).toList();
        return getElasticPathCategoryIdToElasticPathNodeProductsResponse(subcategories, elasticPathCloudCategoryIdToNodeProductsResponses);
    }

    private CompletableFuture<ElasticPathNodeProductsResponse> getNodeProducts(Category category) {
        return elasticPathProductExperienceManagerClient.getNodeProducts(
                category.getElasticPathCloudHierarchyId(),
                category.getElasticPathCloudCategoryId());
    }
}

