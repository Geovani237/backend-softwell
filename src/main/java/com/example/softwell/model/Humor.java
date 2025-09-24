package com.example.softwell.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data; // Anotação do Lombok para getters, setters, etc.

@Data
@Document(collection = "humores")
public class Humor {

    @Id
    private String id;

    private String estadoDeHumor;

    private String emoji;

}