package com.chitova.florist.outbound.accounts.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ElasticPathCloudCreateOneTimePasswordTokenRequest {
    private OtpTokenRequestData data;

    @Builder
    @Data
    public static class OtpTokenRequestData {
        private String type;
        private String username;
        private String purpose;
    }
}
