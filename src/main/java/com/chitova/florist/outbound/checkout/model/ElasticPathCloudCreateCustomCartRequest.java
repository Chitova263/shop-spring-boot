package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticPathCloudCreateCustomCartRequest {
    private DataPayload data;

    @Data
    @Builder
    public static class DataPayload {
        private String name;
        private String description;
        private Contact contact;

        @JsonProperty("discount_settings")
        private DiscountSettings discountSettings;
    }

    @Data
    @Builder
    public static class DiscountSettings {
        @JsonProperty("custom_discounts_enabled")
        private boolean customDiscountsEnabled;
    }

    @Data
    @Builder
    public static class Contact {
        private String email;
    }


}