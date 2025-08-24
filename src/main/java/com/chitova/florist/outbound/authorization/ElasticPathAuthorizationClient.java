package com.chitova.florist.outbound.authorization;

import com.chitova.elasticpathcloud.authentication.model.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ElasticPathAuthorizationClient {
    @Value("${elasticpathcloud.pcm.authUrl}")
    private String authUrl;

    @Value("${elasticpathcloud.pcm.getClientCredentials.path}")
    private String getClientCredentialsPath;

    @Value("${elasticpathcloud.pcm.clientId}")
    private String clientId;

    @Value("${elasticpathcloud.pcm.clientSecret}")
    private String clientSecret;

    private final WebClient webClient;

    public ElasticPathAuthorizationClient(
            final @Qualifier("ElasticPathCloudAuthorizationClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<AccessTokenResponse> getClientCredentials() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(getClientCredentialsPath)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(AccessTokenResponse.class);
    }
}
