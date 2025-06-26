package com.chitova.florist.services;

import com.chitova.florist.entities.Category;
import com.chitova.florist.entities.ChildProduct;
import com.chitova.florist.entities.Product;
import com.chitova.florist.entities.Variation;
import com.chitova.florist.entities.VariationOption;
import com.chitova.florist.outbound.*;
import com.chitova.florist.repositories.CategoryRepository;
import com.chitova.florist.repositories.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductSyncService {

    private final ElasticPathCloudClient elasticPathCloudClient;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductSyncService(final ElasticPathCloudClient elasticPathCloudClient,
                              final CategoryRepository categoryRepository,
                              final ProductRepository productRepository
    ) {
        this.elasticPathCloudClient = elasticPathCloudClient;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public void synchronize() {
        final ElasticPathCloudHierarchiesResponse multiHierarchy = this.elasticPathCloudClient.getHierarchies();
        final ElasticPathCloudHierarchiesResponse.DataItem hierarchy = multiHierarchy.getData().get(0);
        final ElasticPathCloudHierarchyChildNodes multiNodes = elasticPathCloudClient.getHierarchyChildNodes(hierarchy.getId());
        final List<Category> categories = multiNodes.getData()
                .stream()
                .map(node -> Category.builder()
                        .id(new ObjectId())
                        .name(node.getAttributes().getName())
                        .description(node.getAttributes().getDescription())
                        .elasticPathCloudCategoryId(node.getId())
                        .elasticPathCloudHierarchyId(hierarchy.getId())
                        .hasChildren(node.getMeta().isHasChildren())
                        .build())
                .collect(Collectors.toList());
        categoryRepository.saveAll(categories);

        HashMap<String, ArrayList<Category>> productToCategoryMap = new HashMap<>();
        for (Category category : categories) {
            ElasticPathCloudNodeProductsResponse products = elasticPathCloudClient.getNodeProducts(category.getElasticPathCloudHierarchyId(), category.getElasticPathCloudCategoryId());
            for (ElasticPathCloudNodeProductsResponse.Product product : products.getData()) {
                if(!productToCategoryMap.containsKey(product.getId())) {
                    productToCategoryMap.put(product.getAttributes().getSku(), new ArrayList<>(List.of(category)));
                } else {
                    productToCategoryMap.get(product.getAttributes().getSku()).add(category);
                }
            }
        }

        final ElasticPathCloudProductsResponse multiProductResponse = elasticPathCloudClient.getProducts();

        final List<ChildProduct> childProducts = multiProductResponse.getData()
                .stream()
                .filter(product -> !ElasticPathCloudMultiProductsResponseAccessor.isParentProduct(product))
                .map(product -> ChildProduct.builder()
                        .elasticPathCloudProductId(product.getId())
                        .elasticPathCloudParentProductId(ElasticPathCloudMultiProductsResponseAccessor.getElasticPathCloudParentId(product.getRelationships()))
                        .sku(product.getAttributes().getSku())
                        .name(product.getAttributes().getName())
                        .bestseller(false)
                        .information(ElasticPathCloudMultiProductsResponseAccessor
                                .getInformation(product)
                                .orElse(null))
                        .additionalInformation(ElasticPathCloudMultiProductsResponseAccessor
                                .getAdditionalInformation(product)
                                .orElse(null))
                        .variations(product.getMeta().getChildVariations()
                                .stream()
                                .map(childVariation -> Variation.builder()
                                        .elasticPathCloudVariationId(childVariation.getId())
                                        .name(childVariation.getName())
                                        .variationOption(VariationOption.builder()
                                                .elasticPathCloudVariationOptionId(childVariation.getSingleOption().getId())
                                                .name(childVariation.getSingleOption().getName())
                                                .description(childVariation.getSingleOption().getDescription())
                                                .sortOrder(childVariation.getSortOrder())
                                                .build())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build())
                .toList();

        final List<Product> products = multiProductResponse.getData()
                .stream()
                .filter(ElasticPathCloudMultiProductsResponseAccessor::isParentProduct)
                .map(product -> Product.builder()
                        .id(new ObjectId())
                        .sku(product.getAttributes().getSku())
                        .name(product.getAttributes().getName())
                        .bestseller(false)
                        .information(ElasticPathCloudMultiProductsResponseAccessor
                                .getInformation(product)
                                .orElse(null))
                        .additionalInformation(ElasticPathCloudMultiProductsResponseAccessor
                                .getAdditionalInformation(product)
                                .orElse(null))
                        .elasticPathCloudProductId(product.getId())
                        .childProducts(childProducts.stream()
                                .filter(childProduct -> childProduct.getElasticPathCloudParentProductId().equals(product.getId()))
                                .collect(Collectors.toSet()))
                        .categories(new HashSet<>(productToCategoryMap.get(product.getAttributes().getSku())))
                        .build())
                .collect(Collectors.toList());
        productRepository.saveAll(products);
    }
}
