package com.chitova.florist.outbound;

import com.chitova.elasticpathcloud.pim.model.MultiHierarchy;
import com.chitova.elasticpathcloud.pim.model.MultiNodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElasticPathCloudClient {

    private final WebClient.Builder webClientBuilder;
    private final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient;
    private final String baseUrl;
    private final String getHierarchiesPath;
    private final String getHierarchyChildNodesPath;
    private final String getProductsPath;
    private final String getNodeProductsPath;
    private final String getVariationsPath;


    public ElasticPathCloudClient(final WebClient.Builder webClientBuilder,
                                  final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient,
                                  @Value("${elasticpathcloud.pcm.baseUrl}") final String baseUrl,
                                  @Value("${elasticpathcloud.pcm.getHierarchies.path}") final String getHierarchiesPath,
                                  @Value("${elasticpathcloud.pcm.getHierarchyChildNodes.path}") final String getHierarchyChildNodesPath,
                                  @Value("${elasticpathcloud.pcm.getProducts.path}") final String getProductsPath,
                                  @Value("${elasticpathcloud.pcm.getNodeProducts.path}") final String getNodeProductsPath,
                                  @Value("${elasticpathcloud.pcm.getVariations.path}") final String getVariationsPath,
                                  @Value("${elasticpathcloud.pcm.clientId}") final String clientId,
                                  @Value("${elasticpathcloud.pcm.clientSecret}") final String clientSecret
                                  ) {
        this.webClientBuilder = webClientBuilder;
        this.elasticPathCloudAuthorizationClient = elasticPathCloudAuthorizationClient;
        this.baseUrl = baseUrl;
        this.getHierarchiesPath = getHierarchiesPath;
        this.getHierarchyChildNodesPath = getHierarchyChildNodesPath;
        this.getProductsPath = getProductsPath;
        this.getNodeProductsPath = getNodeProductsPath;
        this.getVariationsPath = getVariationsPath;
    }

    public MultiHierarchy getHierarchies() {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get().uri(uriBuilder -> uriBuilder
                        .path(getHierarchiesPath)
                        .build())
                .retrieve()
                .bodyToMono(MultiHierarchy.class)
                .block();
    }

    public MultiNodes getHierarchyChildNodes(final String hierarchyId) {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getHierarchyChildNodesPath)
                        .build(hierarchyId))
                .retrieve()
                .bodyToMono(MultiNodes.class)
                .block();
    }

    public ElasticPathCloudProductsResponse getProducts() {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getProductsPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathCloudProductsResponse.class)
                .block();
    }

    public ElasticPathCloudNodeProductsResponse getNodeProducts(final String hierarchyId, final String nodeId) {
            return webClientBuilder
                    .clone()
                    .baseUrl(baseUrl)
                    .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(getNodeProductsPath)
                            .build(hierarchyId, nodeId))
                    .retrieve()
                    .bodyToMono(ElasticPathCloudNodeProductsResponse.class)
                    .block();
    }

    public ElasticPathCloudVariationsResponse getVariations() {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getVariationsPath)
                        .build())
                .retrieve()
                .bodyToMono(ElasticPathCloudVariationsResponse.class)
                .block();
    }


}
