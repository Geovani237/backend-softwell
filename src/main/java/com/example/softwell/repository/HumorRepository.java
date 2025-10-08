package com.example.softwell.repository;

import com.example.softwell.model.Humor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumorRepository extends MongoRepository<Humor, String> {
}