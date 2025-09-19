package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "emotion")
public class Emotion {
    @Id
    private String id;
    private String emotion;
    private LocalDateTime date;

     public Emotion(String id, String emotion, LocalDateTime data){
         this.id = id;
         this.emotion = emotion;
         this.date = data;
     }

     public Emotion(String emotion){
         this.emotion = emotion;
     }

     public Emotion(){

     }
}
