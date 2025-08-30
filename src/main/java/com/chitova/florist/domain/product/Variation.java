package com.chitova.florist.domain.product;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
public class Variation {
    @Field(name = "elastic_path_cloud_variation_id")
    private String elasticPathCloudVariationId;

    @Field(name = "name")
    private String name;

    @Field(name = "variation_option")
    private VariationOption variationOption;
}
