package com.chitova.florist.repositories;

import com.chitova.florist.entities.configuration.Configuration;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, ObjectId> {
}
