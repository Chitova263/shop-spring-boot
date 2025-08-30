package com.chitova.florist.services.seeder;

import com.chitova.florist.domain.product.Category;
import com.chitova.florist.outbound.products.ElasticPathProductExperienceManagerClient;
import com.chitova.florist.outbound.products.response.ElasticPathCreateProductResponse;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.services.MockProduct;
import com.chitova.florist.services.Seeder;
import com.chitova.florist.outbound.products.ElasticPathProductRequestFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SeederService {

    private final CategoryRepository categoryRepository;
    private final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient;

    public SeederService(final CategoryRepository categoryRepository,
                         final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient) {
        this.categoryRepository = categoryRepository;
        this.elasticPathProductExperienceManagerClient = elasticPathProductExperienceManagerClient;
    }

    public void seedCatalog() throws IOException {
        final List<Category> categories = categoryRepository.findAll();
        final ObjectMapper objectMapper = new ObjectMapper();
        final Seeder seeder = new Seeder(new PathMatchingResourcePatternResolver());
        final HashMap<String, List<MockProduct>> categoryElasticPathCloudIdToMockProducts = new HashMap<String, List<MockProduct>>();
        final List<Resource> resources = seeder.getMockProductResources();
        final List<Category> flattenedCategories = flattenCategories(categories);
        for (final Category category : flattenedCategories) {
            final Optional<Resource> resource = resources.stream()
                    .filter(r -> Objects.equals(r.getFilename(), category.getName() + ".json"))
                    .findFirst();
            if(resource.isPresent()) {
                final List<MockProduct> mockProducts = objectMapper.readValue(resource.get().getInputStream(), new TypeReference<List<MockProduct>>() {});
                categoryElasticPathCloudIdToMockProducts.put(category.getElasticPathCloudCategoryId(), mockProducts);
            }
        }

        final Map<String, Category> categoryElasticPathCloudIdToCategory = flattenedCategories.stream()
                .collect(Collectors.toMap(Category::getElasticPathCloudCategoryId, category -> category));

        final List<CompletableFuture<ElasticPathCreateProductResponse>> createProductRequestsFutures = categoryElasticPathCloudIdToMockProducts.values().stream()
                .flatMap(List::stream)
                .map(ElasticPathProductRequestFactory::createElasticPathCloudCreateProductRequest)
                .map(request -> CompletableFuture.supplyAsync(() ->
                        elasticPathProductExperienceManagerClient.createProduct(request)
                ))
                .toList();

        final List<ElasticPathCreateProductResponse> createProductResponses = CompletableFuture
                .allOf(createProductRequestsFutures.toArray(CompletableFuture[]::new))
                .thenApply(v -> createProductRequestsFutures.stream().map(CompletableFuture::join).toList())
                .join();

        final Map<String, String> createdProductSkuToElasticPathProductId = createProductResponses.stream()
                .collect(Collectors.toMap(
                        p -> p.getData().getAttributes().getSku(),
                        p -> p.getData().getId()
                ));

        final List<CompletableFuture<Void>> nodeRelationshipToProductsFutures = categoryElasticPathCloudIdToMockProducts.entrySet().stream()
                .map(entry -> {
                    final var categoryId = entry.getKey();
                    final var mockProducts = entry.getValue();
                    final var category = categoryElasticPathCloudIdToCategory.get(categoryId);
                    return CompletableFuture.supplyAsync(() ->
                            elasticPathProductExperienceManagerClient.createNodeRelationshipToProducts(
                                    ElasticPathProductRequestFactory.createNodeRelationshipToProductsRequest(mockProducts, createdProductSkuToElasticPathProductId),
                                    category.getElasticPathCloudHierarchyId(),
                                    category.getElasticPathCloudCategoryId()
                            ));
                })
                .toList();

        CompletableFuture
                .allOf(nodeRelationshipToProductsFutures.toArray(CompletableFuture[]::new))
                .join();
    }

    private List<Category> flattenCategories(final List<Category> categories) {
        final List<Category> result = new ArrayList<>();
        for (final Category category : categories) {
            result.add(category);
            if (!category.getSubcategories().isEmpty()) {
                result.addAll(flattenCategories(category.getSubcategories()));
            }
        }
        return result;
    }
}
