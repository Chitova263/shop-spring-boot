package com.chitova.florist.outbound.products.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCreateProductVariationRelationshipRequest {

    private List<DataItem> data;

    @Data
    @Builder
    public static class DataItem {

        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private String id;
    }
}
