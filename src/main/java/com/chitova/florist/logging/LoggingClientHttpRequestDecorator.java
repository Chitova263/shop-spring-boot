package com.chitova.florist.logging;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

public final class LoggingClientHttpRequestDecorator extends ClientHttpRequestDecorator {
    private final WebClientLogger webClientLogger;
    private final Charset charset;

    public LoggingClientHttpRequestDecorator(final ClientHttpRequest delegate, final WebClientLogger webClientLogger, final Charset charset) {
        super(delegate);
        this.webClientLogger = webClientLogger;
        this.charset = charset;
    }

    public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {
        Flux<? extends DataBuffer> loggingBody = Flux.from(body).doOnNext(dataBuffer -> {
            int readPosition = dataBuffer.readPosition();
            byte[] data1 = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(data1);
            dataBuffer.readPosition(readPosition);
            byte[] data = data1;
            this.webClientLogger.requestBody(data, this.charset);
        });

        return super.writeWith(loggingBody);
    }

}