package com.chitova.florist.services.sync;

import com.chitova.florist.entities.product.*;
import com.chitova.florist.outbound.products.ElasticPathProductExperienceManagerClient;
import com.chitova.florist.outbound.products.ElasticPathProductResponseAccessor;
import com.chitova.florist.outbound.products.response.*;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductSyncService {

    private final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductSyncService(final ElasticPathProductExperienceManagerClient elasticPathProductExperienceManagerClient,
                              final CategoryRepository categoryRepository,
                              final ProductRepository productRepository
    ) {
        this.elasticPathProductExperienceManagerClient = elasticPathProductExperienceManagerClient;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public void updateCatalog() {
        final ElasticPathHierarchiesResponse hierarchies = this.elasticPathProductExperienceManagerClient.getHierarchies();
        final ElasticPathHierarchiesResponse.DataItem hierarchy = hierarchies.getData().stream()
                .findFirst()
                .orElseThrow();
        final ElasticPathHierarchyChildNodesResponse hierarchyChildNodes = elasticPathProductExperienceManagerClient.getHierarchyChildNodes(hierarchy.getId());

        final List<Category> categories = hierarchyChildNodes.getData()
                .stream()
                .map(node -> CategoryMapper.toCategory(node, hierarchy.getId()))
                .collect(Collectors.toList());
        populateSubcategoriesRecursively(categories, hierarchy.getId());

        final HashMap<String, ArrayList<Category>> productToCategoryMap = new HashMap<>();
        mapProductToCategoryMap(categories, productToCategoryMap);

        final ElasticPathProductsResponse multiProductResponse = elasticPathProductExperienceManagerClient.getProducts();

        final List<ChildProduct> childProducts = multiProductResponse.getData()
                .stream()
                .filter(ElasticPathProductResponseAccessor::isChildProduct)
                .map(ProductMapper::toChildProduct)
                .toList();

        final List<Product> products = multiProductResponse.getData()
                .stream()
                .filter(ElasticPathProductResponseAccessor::isParentOrStandardProduct)
                .map(product -> ProductMapper.toProduct(product, childProducts, productToCategoryMap))
                .collect(Collectors.toList());

        categoryRepository.deleteAll();
        productRepository.deleteAll();

        categoryRepository.saveAll(categories);
        productRepository.saveAll(products);
    }

    private void populateSubcategoriesRecursively(final List<Category> categories, final String hierarchyId) {
        if(categories.isEmpty()){
            return;
        }
        final var categoriesWithSubcategories = categories.stream()
                .filter(Category::isHasChildren)
                .toList();

        final List<CompletableFuture<ElasticPathNodeChildrenResponse>> futures =
                categoriesWithSubcategories
                        .stream()
                        .map(category -> CompletableFuture.supplyAsync(() ->
                                elasticPathProductExperienceManagerClient.getNodeChildrenResponse(
                                        category.getElasticPathCloudHierarchyId(),
                                        category.getElasticPathCloudCategoryId())))
                        .toList();

        final List<ElasticPathNodeChildrenResponse> nodeChildrenResponses = CompletableFuture
                .allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
                .join();

        // Map each response back to its parent category by index
        IntStream.range(0, categoriesWithSubcategories.size()).forEach(i -> {
            Category parent = categoriesWithSubcategories.get(i);
            ElasticPathNodeChildrenResponse response = nodeChildrenResponses.get(i);
            List<Category> subcategories = response.getData().stream()
                    .map(node -> CategoryMapper.toCategory(node, hierarchyId))
                    .toList();
            parent.getSubcategories().addAll(subcategories);
        });

        var allSubcategories = categoriesWithSubcategories.stream()
                .flatMap(c -> c.getSubcategories().stream())
                .toList();
        populateSubcategoriesRecursively(allSubcategories, hierarchyId);
    }

    private void mapProductToCategoryMap(final List<Category> categories, final HashMap<String, ArrayList<Category>> productToCategoryMap) {
        for (Category category : categories) {
            ElasticPathNodeProductsResponse products = elasticPathProductExperienceManagerClient.getNodeProducts(category.getElasticPathCloudHierarchyId(), category.getElasticPathCloudCategoryId());
            for (ElasticPathNodeProductsResponse.Product product : products.getData()) {
                if(!productToCategoryMap.containsKey(product.getAttributes().getSku())) {
                    productToCategoryMap.put(product.getAttributes().getSku(), new ArrayList<>(List.of(category)));
                } else {
                   var values =  productToCategoryMap.get(product.getAttributes().getSku());
                   values.add(category);
                   productToCategoryMap.replace(product.getAttributes().getSku(), new ArrayList<>(values));
                }
            }
            mapProductToCategoryMap(category.getSubcategories(), productToCategoryMap);
        }
    }
}

