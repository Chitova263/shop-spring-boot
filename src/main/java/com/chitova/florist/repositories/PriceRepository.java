package com.chitova.florist.repositories;

import com.chitova.florist.domain.price.Price;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, ObjectId> {
}
