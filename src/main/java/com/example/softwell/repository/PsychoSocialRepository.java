package com.example.softwell.repository;

import com.example.softwell.model.PsychoSocialAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PsychoSocialRepository extends MongoRepository<PsychoSocialAnswer, String> {

    List<PsychoSocialAnswer> findByUserId(String userId);
}