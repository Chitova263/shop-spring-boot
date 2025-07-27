package com.chitova.florist.logging;

import org.slf4j.MDC;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

import java.nio.charset.Charset;


public class RequestLoggingFilterFunction implements ExchangeFilterFunction {
    private final PropertyResolver propertyResolver;

    public RequestLoggingFilterFunction(final PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }


    @Override
    public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {
        if (LogHelper.isLogEnabled(this.propertyResolver)) {

            WebClientLogger webClientLogger = new WebClientLogger(this.propertyResolver, MDC.getCopyOfContextMap());
            webClientLogger.httpMethod(request.method())
                    .uri(request.url())
                    .requestHeaders(request.headers());

            // https://github.com/spring-projects/spring-framework/issues/24262
            // For low-level request modification (e.g., body, headers, logging) use ClientHttpConnector with a custom ClientHttpRequestDecorator
            // Use ClientHttpRequestDecorator when you want to wrap and customize a ClientHttpRequest
            // BodyInserter is a strategy interface responsible for writing the body of an HTTP request to a ClientHttpRequest.
            BodyInserter<Object, ClientHttpRequest> bodyInserter = new LoggingBodyInserter(request, webClientLogger);

            // Build new request and return
            ClientRequest modifiedRequest = ClientRequest.from(request).body(bodyInserter).build();
            Mono<ClientResponse> clientResponseMono = next.exchange(modifiedRequest)
                    .map(clientResponse -> {
                        Charset charset = LogHelper.getCharset(clientResponse.headers().contentType().orElse(null));

                        // 1. Read the response body into a buffer
                        // 2. Log the response body and rewrite it to the cloned response

                        webClientLogger
                                .statusCode(clientResponse.statusCode())
                                .responseHeaders(clientResponse.headers().asHttpHeaders());
                        return clientResponse.mutate()
                                .body(dataBufferFlux -> {
                                    Flux<DataBuffer> result = dataBufferFlux
                                            .doOnNext(dataBuffer -> {
                                                int readPosition = dataBuffer.readPosition();
                                                byte[] data1 = new byte[dataBuffer.readableByteCount()];
                                                dataBuffer.read(data1);
                                                dataBuffer.readPosition(readPosition);
                                                byte[] data = data1;
                                                String body1 = new String(data, charset);
                                                webClientLogger.responseBody(body1);
                                            }).doFinally(signal -> {
                                                // All the logs are written here
                                                webClientLogger.writeLog();
                                            });
                                    // Return the bytes that were read
                                    return result;
                                })
                                .build();
                    })
                    .doOnError((e) -> webClientLogger.writeLog())
                    .doOnCancel(webClientLogger::writeLog);
            return clientResponseMono;
        }
        Mono<ClientResponse> exchange = next.exchange(request);
        return exchange;
    }






}


