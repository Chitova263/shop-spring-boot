package com.chitova.florist.domain.configuration;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Getter
@Builder
@Document(collection = "configuration")
public class Configuration {
    @MongoId
    @Field(name = "configuration_id")
    private ObjectId id;
    private Set<OpenIdConnectConfiguration> openIdConnectConfiguration;
    private String baseUrl;
}