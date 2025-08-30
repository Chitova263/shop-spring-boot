package com.chitova.florist.domain.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Setter
@Getter
@Builder
@Document(collection = "cart")
public class Cart {
    @MongoId
    @Field(name = "cart_id")
    private ObjectId id;

    @Field(name = "elastic_path_cloud_cart_id")
    private String elasticPathCloudCartId;

    @Indexed(unique = true)
    @Field(name = "customer_id")
    private String customerId;

    @Field(name = "cart_items")
    private List<CartItem> cartLineItems;
}
