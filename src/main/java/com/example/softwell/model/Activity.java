package com.example.softwell.model;

import com.fasterxml.jackson.annotation.JsonProperty; // Importar se necessário
import com.fasterxml.jackson.annotation.JsonIgnore; // Importar se necessário
import com.fasterxml.jackson.annotation.JsonProperty.Access; // Novo import
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activity")
public class Activity {

    @Id
    private String id;
    private String activity;

    // CORREÇÃO CRUCIAL: Diz ao Jackson para não esperar este campo na entrada (POST).
    // O servidor sempre o preencherá no ActivityService.
    @JsonProperty(access = Access.READ_ONLY)
    private LocalDateTime date;
}