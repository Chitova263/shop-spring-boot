package com.chitova.florist.outbound.checkout.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCloudCreateAccountCartAssociationResponse {
    private List<DataItem> data;

    @Data
    @Builder
    public static class DataItem {
        private String id;
        private String type;
    }
}
