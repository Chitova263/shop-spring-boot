package com.chitova.florist.entities.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
public class ChildProduct {
    @Field(name = "elastic_path_cloud_product_id")
    private String elasticPathCloudProductId;

    @Field(name = "elastic_path_cloud_parent_product_id")
    private String elasticPathCloudParentProductId;

    @Field(name = "sku")
    private String sku;

    @Field(name = "name")
    private String name;

    @Field(name = "information")
    private String information;

    @Field(name = "additional_information")
    private String additionalInformation;

    @Field(name = "bestseller")
    private boolean bestseller;

    @Field
    private Set<Variation> variations = new HashSet<>();
}
