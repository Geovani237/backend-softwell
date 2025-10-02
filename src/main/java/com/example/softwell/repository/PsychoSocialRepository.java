package com.example.softwell.repository;

import com.example.softwell.model.PsychoSocialAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PsychoSocialRepository extends MongoRepository<PsychoSocialAnswer, String> {

    // Método para buscar todas as respostas de um usuário específico
    List<PsychoSocialAnswer> findByUserId(String userId);
}