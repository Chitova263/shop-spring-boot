package com.chitova.florist.domain.product;

import lombok.Builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Builder
@Getter
@Setter
@Document(collection = "product")
public class Product {
    @MongoId
    private ObjectId id;

    @NonNull
    @Field(name = "elasticPathCloudProductId")
    private String elasticPathCloudProductId;

    @NonNull
    @Field(name = "sku")
    private String sku;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "slug")
    private String slug;

    @Field(name = "information")
    private String information;

    @Field(name = "additionalInformation")
    private String additionalInformation;

    @Field(name = "bestseller")
    private boolean bestseller;

    @Field(name = "price")
    private double price;

    @Field(name = "listPrice")
    private double listPrice;

    @Field(name = "elasticPathCloudCategoryIds")
    private Set<String> elasticPathCloudCategoryIds;

    @Field(name = "productType")
    private ProductType productType;

    @Field(name = "elasticPathCloudParentProductId")
    private String elasticPathCloudParentProductId;

    @Field(name = "variations")
    private Set<Variation> variations;
}
