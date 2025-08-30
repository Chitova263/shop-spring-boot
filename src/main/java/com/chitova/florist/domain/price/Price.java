package com.chitova.florist.domain.price;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
@Document(collection = "price")
public class Price {
    @MongoId
    @Field(name = "price_id")
    private ObjectId id;

    @Field(name = "elastic_path_cloud_price_id")
    private String elasticPathCloudPriceId;

    @Field(name = "created_at")
    private Instant createdAt;

    @Field(name = "updated_at")
    private Instant updatedAt;

    @Field(name = "price")
    private Map<String, ProductPrice> price;

    @Field(name = "sku")
    private String sku;
}
