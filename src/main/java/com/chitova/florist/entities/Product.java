package com.chitova.florist.entities;

import lombok.Builder;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@Document(collection = "product")
public class Product {
    @Id
    @Field(name = "product_id")
    private ObjectId id;

    @Field(name = "elastic_path_cloud_product_id")
    private String elasticPathCloudProductId;

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

    @Field(name = "child_products")
    private Set<ChildProduct> childProducts;

    @Field(name = "categories")
    private Set<Category> categories = new HashSet<>();
}
