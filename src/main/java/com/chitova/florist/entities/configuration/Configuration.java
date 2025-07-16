package com.chitova.florist.entities.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Setter
@Getter
@Builder
@Document(collection = "configuration")
public class Configuration {
    @Field(name = "configuration_id")
    private ObjectId id;
    private Set<OpenIdConnectConfiguration> openIdConnectConfiguration;
    private String baseUrl;
}