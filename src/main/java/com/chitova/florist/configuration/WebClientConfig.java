package com.chitova.florist.configuration;

import com.chitova.florist.logging.RequestLoggingFilterFunction;
import com.chitova.florist.outbound.authorization.ElasticPathAuthorizationFilterFunction;
import com.chitova.florist.outbound.authorization.ElasticPathAuthorizationClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${elasticpathcloud.pcm.baseUrl}")
    private String pcmBaseUrl;

    @Value("${elasticpathcloud.pcm.authUrl}")
    private String authUrl;

    @Bean
    public WebClient.Builder webClientBuilder(final RequestLoggingFilterFunction requestLoggingFilterFunction) {
        return WebClient.builder().filter(requestLoggingFilterFunction);
    }

    @Bean
    public RequestLoggingFilterFunction requestLoggingFilterFunction(final Environment environment) {
        return new RequestLoggingFilterFunction(environment);
    }

    @Bean
    @Qualifier("ElasticPathProductExperienceManagerClient")
    public WebClient elasticPathProductExperienceManagerWebClient(final WebClient.Builder webClientBuilder, final ElasticPathAuthorizationFilterFunction  elasticPathAuthorizationFilterFunction) {
        return webClientBuilder
                .baseUrl(pcmBaseUrl)
                .filter(elasticPathAuthorizationFilterFunction)
                .build();
    }

    @Bean
    @Qualifier("ElasticPathCloudAuthorizationClient")
    public WebClient elasticPathCloudAuthorizationWebClient(final WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(authUrl)
                .build();
    }

    @Bean
    public ElasticPathAuthorizationFilterFunction elasticPathAuthorizationFilterFunction(final ElasticPathAuthorizationClient elasticPathCloudAuthorizationClient) {
        return new ElasticPathAuthorizationFilterFunction(elasticPathCloudAuthorizationClient);
    }

}
