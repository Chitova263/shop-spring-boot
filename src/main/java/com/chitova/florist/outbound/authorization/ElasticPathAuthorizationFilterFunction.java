package com.chitova.florist.outbound.authorization;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public record ElasticPathAuthorizationFilterFunction(
        ElasticPathAuthorizationClient elasticPathCloudAuthorizationClient) implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return elasticPathCloudAuthorizationClient.getClientCredentials()
                .flatMap(credentials -> {
                    final ClientRequest modifiedRequest = ClientRequest.from(request)
                            .header("Authorization", "Bearer " + credentials.getAccessToken())
                            .build();
                    return next.exchange(modifiedRequest);
                });
    }
}
