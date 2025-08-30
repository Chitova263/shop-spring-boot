package com.chitova.florist.repositories;

import com.chitova.florist.domain.pricebook.Pricebook;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricebookRepository extends MongoRepository<Pricebook, ObjectId> {
}
