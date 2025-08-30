package com.chitova.florist.domain.pricebook;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Builder
@Getter
@Document("pricebook")
public class Pricebook {
    @MongoId
    @Field(name = "pricebook_id")
    private ObjectId id;

    @Field(name = "elastic_path_cloud_pricebook_id")
    private String elasticPathCloudPricebookId;

    @Field(name = "description")
    private String description;

    @Field(name = "name")
    private String name;

    @Field(name = "updated_at")
    private Instant updatedAt;
}
