package com.chitova.florist.repositories;

import com.chitova.florist.domain.cart.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, ObjectId> {
    Optional<Cart> findByCustomerId(final String customerId);
}
