package com.chitova.florist.configuration;

import com.chitova.florist.logging.RequestLoggingFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder(RequestLoggingFilterFunction requestLoggingFilterFunction) {
        return WebClient.builder().filter(requestLoggingFilterFunction);
    }

    @Bean
    public RequestLoggingFilterFunction requestLoggingFilterFunction(Environment environment) {
        return new RequestLoggingFilterFunction(environment);
    }
}
