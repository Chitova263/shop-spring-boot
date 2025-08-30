package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ElasticPathProductPricesResponse {
    private List<ProductPriceData> data;
    private Links links;
    private Meta meta;

    @Data
    public static class ProductPriceData {
        private String id;
        private String type;
        private Attributes attributes;
        private OwnerMeta meta;
    }

    @Data
    public static class Attributes {
        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        private Map<String, Currency> currencies;
        private Sales sales;
        private String sku;
    }

    @Data
    public static class Currency {
        private double amount;

        @JsonProperty("includes_tax")
        private boolean includesTax;
    }

    @Data
    public static class Sales {
        @JsonProperty("default")
        private DefaultSale defaultSale;
    }

    @Data
    public static class DefaultSale {
        private Map<String, Currency> currencies;
    }

    @Data
    public static class OwnerMeta {
        private String owner;
    }

    @Data
    public static class Links {
        private String first;
        private String last;
        private String self;
    }

    @Data
    public static class Meta {
        private Page page;
        private Results results;
    }

    @Data
    public static class Page {
        private int current;
        private int limit;
        private int offset;
        private int total;
    }

    @Data
    public static class Results {
        private int total;
    }
}
