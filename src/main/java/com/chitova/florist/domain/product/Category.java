package com.chitova.florist.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.bson.types.ObjectId;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Builder
@Document(collection = "category")
public class Category {
    @MongoId
    private ObjectId id;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "slug")
    private String slug;

    @NonNull
    @Field(name = "elasticPathCloudCategoryId")
    private String elasticPathCloudCategoryId;

    @NonNull
    @Field(name = "elasticPathCloudHierarchyId")
    private String elasticPathCloudHierarchyId;

    @Field(name = "subcategories")
    private List<Category> subcategories;

    @Transient
    private boolean hasSubcategories;
}
