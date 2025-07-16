package com.chitova.florist.outbound.checkout.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCloudCreateAccountCartAssociationRequest {
    private List<Resource> data;

    @Data
    @Builder
    public static class Resource {
        private String id;
        private String type;
    }
}
