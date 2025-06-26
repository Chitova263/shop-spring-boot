package com.chitova.florist.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudClientCredentialsResponse {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires")
    private long expires;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("identifier")
    private String identifier;
}
