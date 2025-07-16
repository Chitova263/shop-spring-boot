package com.chitova.florist.outbound.checkout;

import com.chitova.florist.outbound.authorization.ElasticPathCloudAuthorizationClient;
import com.chitova.florist.outbound.checkout.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElasticPathCloudCheckoutClient {

    public static final String EP_ACCOUNT_MANAGEMENT_AUTHENTICATION_TOKEN = "EP-Account-Management-Authentication-Token";
    private final WebClient.Builder webClientBuilder;
    private final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient;
    private final String baseUrl;
    private final String createCustomCartPath;
    private final String createAccountCartAssociationPath;
    private final String createBulkAddItemsToCartPath;
    private final String checkoutPath;


    public ElasticPathCloudCheckoutClient(final WebClient.Builder webClientBuilder,
                                          final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient,
                                          @Value("${elasticpathcloud.accounts.baseUrl}") final String baseUrl,
                                          @Value("${elasticpathcloud.carts.createCustomCart.path}") final String createCustomCartPath,
                                          @Value("${elasticpathcloud.carts.createAccountCartAssociation.path}") final String createAccountCartAssociationPath,
                                          @Value("${elasticpathcloud.carts.createBulkAddItemsToCart.path}") final String createBulkAddItemsToCartPath,
                                          @Value("${elasticpathcloud.carts.checkoutPath.path}") final String checkoutPath
                                  ) {
        this.webClientBuilder = webClientBuilder;
        this.elasticPathCloudAuthorizationClient = elasticPathCloudAuthorizationClient;
        this.baseUrl = baseUrl;
        this.createCustomCartPath = createCustomCartPath;
        this.createAccountCartAssociationPath = createAccountCartAssociationPath;
        this.createBulkAddItemsToCartPath = createBulkAddItemsToCartPath;
        this.checkoutPath = checkoutPath;
    }

    public ElasticPathCloudCreateCustomCartResponse createCustomCart(ElasticPathCloudCreateCustomCartRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder.path(createCustomCartPath).build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudCreateCustomCartResponse.class)
                .block();
    }

    public ElasticPathCloudCreateAccountCartAssociationResponse createAccountCartAssociation(
            final String cartId,
            final String accountManagementToken,
            final ElasticPathCloudCreateAccountCartAssociationRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder.path(createAccountCartAssociationPath).build(cartId))
                .header(EP_ACCOUNT_MANAGEMENT_AUTHENTICATION_TOKEN, accountManagementToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudCreateAccountCartAssociationResponse.class)
                .block();
    }

    public ElasticPathCloudBulkAddItemsToCartResponse bulkAddItemsToCart(
            final String cartId,
            final ElasticPathCloudBulkAddItemsToCartRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder.path(createBulkAddItemsToCartPath).build(cartId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudBulkAddItemsToCartResponse.class)
                .block();
    }

    public ElasticPathCloudCheckoutResponse checkoutWithAccountManagementToken(
            final String cartId,
            final String accountManagementToken,
            final ElasticPathCloudCheckoutWithAccountManagementTokenRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder.path(checkoutPath).build(cartId))
                .header(EP_ACCOUNT_MANAGEMENT_AUTHENTICATION_TOKEN, accountManagementToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudCheckoutResponse.class)
                .block();
    }

    public ElasticPathCloudCheckoutResponse checkoutWithAccountManagementToken(
            final String cartId,
            final ElasticPathCloudCheckoutAsGuestRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder.path(checkoutPath).build(cartId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudCheckoutResponse.class)
                .block();
    }
}
