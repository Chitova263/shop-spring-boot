package com.chitova.florist.outbound.products;

import com.chitova.florist.outbound.products.request.ElasticPathCreateNodeRelationshipToProductsRequest;
import com.chitova.florist.outbound.products.request.ElasticPathCreateProductRequest;
import com.chitova.florist.outbound.products.request.ElasticPathCreateProductVariationRelationshipRequest;
import com.chitova.florist.outbound.products.response.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
public class ElasticPathProductExperienceManagerClient {

    @Value("${elasticpathcloud.pcm.baseUrl}")
    private String baseUrl;

    @Value("${elasticpathcloud.pcm.getHierarchies.path}")
    private String getHierarchiesPath;

    @Value("${elasticpathcloud.pcm.getHierarchyChildNodes.path}")
    private String getHierarchyChildNodesPath;

    @Value("${elasticpathcloud.pcm.getProducts.path}")
    private String getProductsPath;

    @Value("${elasticpathcloud.pcm.getNodeProducts.path}")
    private String getNodeProductsPath;

    @Value("${elasticpathcloud.pcm.getNodeChildren.path}")
    private String getNodeChildrenPath;

    @Value("${elasticpathcloud.pcm.getVariations.path}")
    private String getVariationsPath;

    @Value("${elasticpathcloud.pcm.createProduct.path}")
    private String createProductPath;

    @Value("${elasticpathcloud.pcm.associateNodeToProducts.path}")
    private String associateNodeToProductsPath;

    @Value("${elasticpathcloud.pcm.getProductPrices.path}")
    private String getProductPricesPath;

    @Value("${elasticpathcloud.pcm.getPriceBooks.path}")
    private String getPriceBooksPath;

    @Value("${elasticpathcloud.pcm.createProductVariationRelationship.path}")
    private String createProductVariationRelationshipPath;

    private final WebClient webClient;

    public ElasticPathProductExperienceManagerClient(final @Qualifier("ElasticPathProductExperienceManagerClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ElasticPathHierarchiesResponse getHierarchies() {
        return webClient
                .get().uri(uriBuilder -> uriBuilder
                        .path(getHierarchiesPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathHierarchiesResponse.class)
                .block();
    }

    @Async
    public CompletableFuture<ElasticPathHierarchyChildNodesResponse> getHierarchyChildNodes(final String hierarchyId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getHierarchyChildNodesPath)
                        .build(hierarchyId))
                .retrieve()
                .bodyToMono(ElasticPathHierarchyChildNodesResponse.class)
                .toFuture();
    }

    @Async
    public CompletableFuture<ElasticPathNodeChildrenResponse> getNodeChildrenResponse(final String hierarchyId, final String nodeId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getNodeChildrenPath)
                        .build(hierarchyId, nodeId))
                .retrieve()
                .bodyToMono(ElasticPathNodeChildrenResponse.class)
                .toFuture();
    }

    public ElasticPathProductsResponse getProducts() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getProductsPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathProductsResponse.class)
                .block();
    }

    public ElasticPathCreateProductResponse createProduct(ElasticPathCreateProductRequest request) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createProductPath)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCreateProductResponse.class)
                .block();
    }

    public Void createNodeRelationshipToProducts(ElasticPathCreateNodeRelationshipToProductsRequest request, String hierarchyId, String nodeId) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(associateNodeToProductsPath)
                        .build(hierarchyId, nodeId))
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .block();
    }

    @Async
    public CompletableFuture<ElasticPathNodeProductsResponse> getNodeProducts(final String hierarchyId, final String nodeId) {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(getNodeProductsPath)
                            .build(hierarchyId, nodeId))
                    .retrieve()
                    .bodyToMono(ElasticPathNodeProductsResponse.class)
                    .toFuture();
    }

    public ElasticPathVariationsResponse getVariations() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getVariationsPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathVariationsResponse.class)
                .block();
    }

    public ElasticPathProductPricesResponse getProductPrices(final String priceBookId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getProductPricesPath)
                        .build(priceBookId))
                .retrieve()
                .bodyToMono(ElasticPathProductPricesResponse.class)
                .block();
    }

    public ElasticPathPricebooksResponse getPricebooks() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getPriceBooksPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathPricebooksResponse.class)
                .block();
    }

    public Void createProductVariationRelationship(final ElasticPathCreateProductVariationRelationshipRequest request,
                                                   final String productId) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createProductVariationRelationshipPath)
                        .build(productId))
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .block();
    }
}
