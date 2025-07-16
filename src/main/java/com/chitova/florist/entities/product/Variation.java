package com.chitova.florist.entities.product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
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
