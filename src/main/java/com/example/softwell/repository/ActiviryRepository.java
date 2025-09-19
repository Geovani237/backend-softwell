package com.example.softwell.repository;

import com.example.softwell.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActiviryRepository extends MongoRepository<Activity, String> {
}
