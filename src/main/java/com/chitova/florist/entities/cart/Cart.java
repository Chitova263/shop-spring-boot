package com.chitova.florist.entities.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Builder
@Document(collection = "cart")
public class Cart {

    @Id
    @Field(name = "cart_id")
    private ObjectId id;

    @Field(name = "elastic_path_cloud_cart_id")
    private String elasticPathCloudCartId;

    @Field(name = "cart_items")
    private List<CartItem> cartItems;

    @Field(name = "user_id")
    private String userId;
}
