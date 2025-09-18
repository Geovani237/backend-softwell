package com.example.softwell.service;

import com.example.softwell.model.Emotion;
import com.example.softwell.repository.EmotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionService {

    //Está Emotion vai se bem provavél utilizado pel o ADMIN, para criar, excluir, alterar
    //porem deve retornar? desta classe para o front ou deve fazer em outro lugar ?

    //findAll
    //saveEmotion
    //deleteEmotion
    //updateEmotion
    @Autowired
    private EmotionRepository emotionRepository;

    public List<Emotion> findAll(){
        return emotionRepository.findAll();
    }

    public Emotion saveEmotion(Emotion emotion) {
        return emotionRepository.save(emotion);
    }

    public void deleteEmotion(String id){
        emotionRepository.deleteById(id);
    }

    public Emotion updateEmotion(Emotion emotion){
        return emotionRepository.save(emotion);
    }
}
