package com.example.softwell.repository;

import com.example.softwell.model.UserHumorResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime; // ✅ Importar
import java.util.List; // ✅ Importar
import java.util.Optional;

public interface UserHumorResponseRepository extends MongoRepository<UserHumorResponse, String> {
    Optional<UserHumorResponse> findTopByUserIdOrderByDataRespostaDesc(String userId);

    List<UserHumorResponse> findByDataRespostaBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
