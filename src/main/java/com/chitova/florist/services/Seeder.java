package com.chitova.florist.services;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Seeder {

    public static final String CLASSPATH_SEED_PRODUCTS = "classpath:seed/*";
    private final PathMatchingResourcePatternResolver resolver;

    public Seeder(final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver) {
        this.resolver = pathMatchingResourcePatternResolver;
    }

    @SneakyThrows
    public List<Resource> getMockProductResources() {
        return Arrays.stream(resolver.getResources(CLASSPATH_SEED_PRODUCTS))
                .collect(Collectors.toList());
    }

    public List<MockProduct> seeder(final String category) {
        return List.of();
    }
}
