package com.chitova.florist.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class WebClientLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    public static ExchangeFilterFunction logResponseBody() {
        return ExchangeFilterFunction.ofResponseProcessor(response ->
                response.bodyToMono(String.class)
                        .flatMap(body -> {
                            logger.info("Response body: {}", body);

                            // Rebuild ClientResponse with buffered body so downstream can consume it
                            ClientResponse newResponse = ClientResponse.create(response.statusCode())
                                    .headers(headers -> headers.addAll(response.headers().asHttpHeaders()))
                                    .body(body)
                                    .build();

                            return Mono.just(newResponse);
                        })
        );
    }
}