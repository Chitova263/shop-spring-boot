package com.chitova.florist.outbound.accounts.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ElasticPathCloudCreateUserAuthenticationInfoRequest {

    private UserAuthenticationInfo data;

    @Builder
    @Data
    public static class UserAuthenticationInfo {
        private String type;
        private String name;
        private String email;
    }
}
