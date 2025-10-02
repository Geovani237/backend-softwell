package com.example.softwell.repository;

import com.example.softwell.model.UserHumorResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHumorResponseRepository extends MongoRepository<UserHumorResponse, String> {
}