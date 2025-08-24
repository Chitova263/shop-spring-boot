package com.chitova.florist.outbound.products.request;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ElasticPathCreateNodeRelationshipToProductsRequest {
    private List<DataNode> data;

    @Data
    @Builder
    public static class DataNode {
        private String type;
        private String id;
    }
}
