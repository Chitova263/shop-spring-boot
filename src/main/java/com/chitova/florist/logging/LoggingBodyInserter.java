package com.chitova.florist.logging;

import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

public final class LoggingBodyInserter implements BodyInserter<Object, ClientHttpRequest> {
    private final ClientRequest clientRequest;
    private final WebClientLogger webClientLogger;

    public LoggingBodyInserter(final ClientRequest clientRequest, final WebClientLogger webClientLogger) {
        this.clientRequest = clientRequest;
        this.webClientLogger = webClientLogger;
    }

    @Override
    public Mono<Void> insert(final ClientHttpRequest clientHttpRequest, final BodyInserter.Context context) {
        Charset charset = LogHelper.getCharset(this.clientRequest.headers().getContentType());

        // Create a logging decorator that wraps the ClientHttpRequest
        LoggingClientHttpRequestDecorator loggingClientHttpRequestDecorator = new LoggingClientHttpRequestDecorator(clientHttpRequest, this.webClientLogger, charset);

        // Delegate to the original body inserter, but with the decorated request
        return this.clientRequest
                .body()
                .insert(loggingClientHttpRequestDecorator, context);
    }
}