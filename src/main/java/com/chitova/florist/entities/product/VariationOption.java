package com.chitova.florist.entities.product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
public class VariationOption {
    @Field(name = "elastic_path_cloud_variation_option_id")
    private String elasticPathCloudVariationOptionId;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "sort_order")
    private int sortOrder;
}