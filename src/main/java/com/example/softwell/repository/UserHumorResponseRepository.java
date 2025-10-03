package com.example.softwell.repository;

import com.example.softwell.model.UserHumorResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserHumorResponseRepository extends MongoRepository<UserHumorResponse, String> {
    Optional<UserHumorResponse> findTopByUserIdOrderByDataRespostaDesc(String userId);
}