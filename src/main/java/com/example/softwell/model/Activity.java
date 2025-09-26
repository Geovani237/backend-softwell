package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
//import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "activity")
public class Activity {
    @Id // ou Colocar @MongoId
    private String id;
    private String activity;
    private LocalDateTime date;

    public Activity(String id,String activity, LocalDateTime date) {
        this.id = id;
        this.activity = activity;
        this.date = date;
    }
    public Activity(String activity) {
        this.activity = activity;
    }

    public Activity(){

    }

}
