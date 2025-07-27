package com.chitova.florist.repositories;

import com.chitova.florist.entities.cart.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, ObjectId> {
}
