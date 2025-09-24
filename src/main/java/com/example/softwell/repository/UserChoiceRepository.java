package com.example.softwell.repository;

import com.example.softwell.model.UserChoice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserChoiceRepository extends MongoRepository<UserChoice, String> {

    Optional<UserChoice> findTopByUserIdOrderBySelectedDataDesc(String userId);
}
