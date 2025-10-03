package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "psychosocial_answers")
public class PsychoSocialAnswer {

    @Id
    private String id;
    private String userId;

    // ✅ CORREÇÃO: Campo renomeado para "createdAt" e anotado para ser preenchido automaticamente
    @CreatedDate
    private LocalDateTime createdAt;

    // Submodelos que correspondem à estrutura do frontend
    private Workload workload;
    private WarningSigns warningSigns;
    private RelationshipClimate relationshipClimate;
    private Communication communication;
    private LeadershipRelation leadershipRelation;
}

