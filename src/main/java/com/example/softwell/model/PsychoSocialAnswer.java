package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data // Gera Getters, Setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@Document(collection = "psychosocial_answers")
public class PsychoSocialAnswer {

    @Id
    private String id;

    // ID do usuário que respondeu (Campo crucial)
    private String userId;

    private LocalDateTime submissionDate = LocalDateTime.now();

    // Submodelos agrupados
    private Workload workload;
    private WarningSigns warningSigns;
    private RelationshipClimate relationshipClimate;
    private Communication communication;
    private LeadershipRelation leadershipRelation;
}