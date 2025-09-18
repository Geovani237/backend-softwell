package com.example.softwell.repository;

import com.example.softwell.model.Emotion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmotionRepository extends MongoRepository<Emotion,String> {

}
