package com.chitova.florist.entities.product;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Builder
@Document(collection = "category")
public class Category {
    @Id
    @Field(name = "category_id")
    private ObjectId id;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "elastic_path_cloud_category_id")
    private String elasticPathCloudCategoryId;

    @Field(name = "elastic_path_cloud_hierarchy_id")
    private String elasticPathCloudHierarchyId;

    @Field(name = "has_children")
    private boolean hasChildren;
}
