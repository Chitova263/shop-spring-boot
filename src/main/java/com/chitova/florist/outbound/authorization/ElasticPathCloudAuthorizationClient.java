package com.chitova.florist.outbound.authorization;

import com.chitova.elasticpathcloud.authentication.model.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ElasticPathCloudAuthorizationClient {

    private static final String GRANT_TYPE = "client_credentials";

    private final WebClient.Builder webClientBuilder;
    private final String authUrl;
    private final String getClientCredentialsPath;
    private final String clientId;
    private final String clientSecret;

    public ElasticPathCloudAuthorizationClient(final WebClient.Builder webClientBuilder,
                                  @Value("${elasticpathcloud.pcm.authUrl}") final String authUrl,
                                  @Value("${elasticpathcloud.pcm.getClientCredentials.path}") final String getClientCredentialsPath,
                                  @Value("${elasticpathcloud.pcm.clientId}") final String clientId,
                                  @Value("${elasticpathcloud.pcm.clientSecret}") final String clientSecret
    ) {
        this.webClientBuilder = webClientBuilder;
        this.authUrl = authUrl;
        this.getClientCredentialsPath = getClientCredentialsPath;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Mono<AccessTokenResponse> getClientCredentials() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", ElasticPathCloudAuthorizationClient.GRANT_TYPE);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return webClientBuilder
                .baseUrl(authUrl)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(getClientCredentialsPath)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(AccessTokenResponse.class);
    }

    public  ExchangeFilterFunction authorizationHeaderFilter() {
        return (request, next) -> getClientCredentials()
                .flatMap(elasticPathCloudClientCredentialsResponse -> {
                    final ClientRequest modifiedRequest = ClientRequest.from(request)
                            .header("Authorization", "Bearer " + elasticPathCloudClientCredentialsResponse.getAccessToken())
                            .build();
                    return next.exchange(modifiedRequest);
                });
    }
}
