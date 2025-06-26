package com.chitova.florist.outbound;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElasticPathCloudBaseProduct {

    private BaseProductData data;

    @Data
    @NoArgsConstructor
    public static class BaseProductData{
        private String id;
        private String type;
    }
}
