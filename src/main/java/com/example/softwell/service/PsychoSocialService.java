package com.example.softwell.service;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.repository.PsychoSocialRepository;
import com.example.softwell.util.MongoAggregationUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document; // ✅ Importe Document
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression; // ✅ Importe AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Service;

import java.util.Arrays; // ✅ Importe Arrays
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@Service
@RequiredArgsConstructor
public class PsychoSocialService {

    private final PsychoSocialRepository repository;
    private final MongoTemplate mongoTemplate;

    // =========================================================
    // ✅ MÉTODO AUXILIAR PARA CRIAR A EXPRESSÃO $AVG CORRETAMENTE
    // Isso injeta a sintaxe pura do MongoDB, contornando o SpEL.
    // =========================================================
    private static AggregationExpression avgOfFields(String... fields) {
        // Constrói um objeto MongoDB Document: { $avg: ["$field1", "$field2", ...] }
        return context -> new Document("$avg", Arrays.asList(fields));
    }
    // =========================================================

    public PsychoSocialAnswer saveAnswer(PsychoSocialAnswer answer) {
        return repository.save(answer);
    }

    public List<PsychoSocialAnswer> getAnswersByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public Map<String, Double> calculateLatestThematicAverages(String userId) {

        MatchOperation matchUser = match(where("userId").is(userId));
        SortOperation sortByDate = sort(Sort.Direction.DESC, "_id");
        LimitOperation limitOne = limit(1);

        // 4. PROJEÇÃO e CÁLCULO DAS MÉDIAS
        ProjectionOperation projectAndCalculate = project()
                // CARGA DE TRABALHO: Projeção dos campos para INT
                .and(MongoAggregationUtils.convertStringToInt("$workload.workloadAssessment")).as("wlA")
                .and(MongoAggregationUtils.convertStringToInt("$workload.qualityOfLifeImpact")).as("wlQ")
                .and(MongoAggregationUtils.convertStringToInt("$workload.extraHours")).as("wlE")
                // ✅ CHAMADA CORRIGIDA: Usa o método auxiliar para criar a expressão $avg
                .and(avgOfFields("$wlA", "$wlQ", "$wlE")).as("avgWorkload")


                // SINAIS DE ALERTA: Projeção dos campos para INT
                .and(MongoAggregationUtils.convertStringToInt("$warningSigns.warningSigns")).as("wsW")
                .and(MongoAggregationUtils.convertStringToInt("$warningSigns.mentalHealthImpact")).as("wsM")
                // ✅ CHAMADA CORRIGIDA
                .and(avgOfFields("$wsW", "$wsM")).as("avgWarningSigns")


                // CLIMA/RELACIONAMENTO (7 campos)
                // Usamos os caminhos originais do documento
                .and(avgOfFields(
                        "$relationshipClimate.bossRating",
                        "$relationshipClimate.coworkerRating",
                        "$relationshipClimate.coworkerRespect",
                        "$relationshipClimate.teamRelationship",
                        "$relationshipClimate.freedomSpeech",
                        "$relationshipClimate.welcomedPart",
                        "$relationshipClimate.cooperationSpirit"
                )).as("avgRelationshipClimate")


                // COMUNICAÇÃO (4 campos)
                .and(avgOfFields(
                        "$communication.taskClarity",
                        "$communication.openCommunication",
                        "$communication.infoFlow",
                        "$communication.goalClarity"
                )).as("avgCommunication")


                // LIDERANÇA (5 campos)
                .and(avgOfFields(
                        "$leadershipRelation.leaderCaresWellbeing",
                        "$leadershipRelation.leaderIsAvailable",
                        "$leadershipRelation.comfortableReportingIssues",
                        "$leadershipRelation.leaderRecognizesEfforts",
                        "$leadershipRelation.trustAndTransparency"
                )).as("avgLeadershipRelation");


        // 5. AGRUPAR (Consolida o resultado de uma linha)
        GroupOperation groupResult = group()
                .avg("avgWorkload").as("avgWorkload")
                .avg("avgWarningSigns").as("avgWarningSigns")
                .avg("avgRelationshipClimate").as("avgRelationshipClimate")
                .avg("avgCommunication").as("avgCommunication")
                .avg("avgLeadershipRelation").as("avgLeadershipRelation");


        Aggregation aggregation = newAggregation(matchUser, sortByDate, limitOne, projectAndCalculate, groupResult);

        // 6. Execução e Retorno
        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "psychoSocialAnswer", Map.class
        );

        Map<String, Object> rawResult = results.getUniqueMappedResult();
        if (rawResult == null || rawResult.isEmpty()) {
            return Map.of();
        }

        Map<String, Double> finalAverages = new HashMap<>();
        rawResult.forEach((key, value) -> {
            if (value instanceof Number && !key.equals("_id")) {
                finalAverages.put(key, ((Number) value).doubleValue());
            }
        });

        return finalAverages;
    }

    public Map<String, Double> calculateThematicAverages() {
        return Map.of();
    }
}