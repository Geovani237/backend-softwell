package com.example.softwell.repository;

import com.example.softwell.model.UserChoice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserChoiceRepository extends MongoRepository<UserChoice, String> {

    // Método essencial para a contagem de votos no relatório
    long countByActivityId(String activityId);

    // Mantendo o método de humor/usuário se for necessário
    Optional<UserChoice> findTopByUserIdOrderBySelectedDataDesc(String userId);
}