package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticPathCloudCheckoutAsGuestRequest {
    private DataPayload data;

    @Data
    @Builder
    public static class DataPayload {
        private Customer customer;

        @JsonProperty("billing_address")
        private Address billingAddress;

        @JsonProperty("shipping_address")
        private Address shippingAddress;
    }

    @Data
    @Builder
    public static class Customer {
        private String name;
        private String email;
    }

    @Data
    @Builder
    public static class Address {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("company_name")
        private String companyName;

        @JsonProperty("line_1")
        private String line1;

        @JsonProperty("line_2")
        private String line2;

        private String county;
        private String region;
        private String postcode;
        private String country;
    }
}
