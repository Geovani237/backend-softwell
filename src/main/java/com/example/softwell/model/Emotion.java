package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "emotion")
public class Emotion {
    @Id
    private String id;
    private String emotion;

     public Emotion(String id, String emotion){
         this.id = id;
         this.emotion = emotion;
     }

     public Emotion(String emotion){
         this.emotion = emotion;
     }

     public Emotion(){

     }
}
