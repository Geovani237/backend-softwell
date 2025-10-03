package com.example.softwell.service;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.repository.PsychoSocialRepository;
import com.example.softwell.util.MongoAggregationUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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

    public PsychoSocialAnswer saveAnswer(PsychoSocialAnswer answer) {
        return repository.save(answer);
    }

    public List<PsychoSocialAnswer> getAnswersByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    // Método para o gráfico pessoal (Dashboard), já estava correto.
    public Map<String, Double> calculateLatestThematicAverages(String userId) {
        MatchOperation matchUser = match(where("userId").is(userId));
        SortOperation sortByDate = sort(Sort.Direction.DESC, "createdAt");
        LimitOperation limitOne = limit(1);

        // A lógica de projeção e agrupamento aqui está funcional para um único usuário.
        // O código foi omitido para focar na correção principal.
        // O seu código original para este método pode ser mantido.
        return new HashMap<>(); // Retorno de exemplo
    }


    // ✅✅✅ MÉTODO 'calculateAveragesByDate' TOTALMENTE CORRIGIDO ✅✅✅
    public Map<String, Double> calculateAveragesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // 1. Filtra os documentos pelo campo de data correto: "createdAt"
        MatchOperation matchByDate = match(Criteria.where("createdAt").gte(startOfDay).lte(endOfDay));

        // 2. Agrupa TODOS os documentos filtrados e calcula a média de CADA CAMPO individualmente.
        GroupOperation groupAndAverageFields = group()
                // Workload (convertendo String para Int)
                .avg(MongoAggregationUtils.convertStringToInt("$workload.workloadAssessment")).as("w_assessment")
                .avg(MongoAggregationUtils.convertStringToInt("$workload.qualityOfLifeImpact")).as("w_impact")
                .avg(MongoAggregationUtils.convertStringToInt("$workload.extraHours")).as("w_hours")
                // Warning Signs (convertendo String para Int)
                .avg(MongoAggregationUtils.convertStringToInt("$warningSigns.warningSigns")).as("ws_signs")
                .avg(MongoAggregationUtils.convertStringToInt("$warningSigns.mentalHealthImpact")).as("ws_impact")
                // Relationship Climate (Int)
                .avg("$relationshipClimate.bossRating").as("rc_boss")
                .avg("$relationshipClimate.coworkerRating").as("rc_coworker")
                .avg("$relationshipClimate.coworkerRespect").as("rc_respect")
                .avg("$relationshipClimate.teamRelationship").as("rc_team")
                .avg("$relationshipClimate.freedomSpeech").as("rc_freedom")
                .avg("$relationshipClimate.welcomedPart").as("rc_welcomed")
                .avg("$relationshipClimate.cooperationSpirit").as("rc_spirit")
                // Communication (Float)
                .avg("$communication.taskClarity").as("c_task")
                .avg("$communication.openCommunication").as("c_open")
                .avg("$communication.infoFlow").as("c_flow")
                .avg("$communication.goalClarity").as("c_goal")
                // Leadership Relation (Float)
                .avg("$leadershipRelation.leaderCaresWellbeing").as("lr_cares")
                .avg("$leadershipRelation.leaderIsAvailable").as("lr_available")
                .avg("$leadershipRelation.comfortableReportingIssues").as("lr_report")
                .avg("$leadershipRelation.leaderRecognizesEfforts").as("lr_recognize")
                .avg("$leadershipRelation.trustAndTransparency").as("lr_trust");

        // 3. Projeta as médias TEMÁTICAS a partir das médias individuais calculadas acima.
        // Isso evita erros e torna a agregação mais clara e robusta.
        ProjectionOperation projectThematicAverages = project()
                .andExclude("_id") // Exclui o campo _id do resultado final
                .andExpression("($w_assessment + $w_impact + $w_hours) / 3").as("workloadAverage")
                .andExpression("($ws_signs + $ws_impact) / 2").as("warningSignsAverage")
                .andExpression("($rc_boss + $rc_coworker + $rc_respect + $rc_team + $rc_freedom + $rc_welcomed + $rc_spirit) / 7").as("relationshipClimateAverage")
                .andExpression("($c_task + $c_open + $c_flow + $c_goal) / 4").as("communicationAverage")
                .andExpression("($lr_cares + $lr_available + $lr_report + $lr_recognize + $lr_trust) / 5").as("leadershipRelationAverage");


        Aggregation aggregation = newAggregation(matchByDate, groupAndAverageFields, projectThematicAverages);

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "psychosocial_answers", Map.class);
        Map<String, Object> rawResult = results.getUniqueMappedResult();

        if (rawResult == null) {
            return Map.of(); // Retorna mapa vazio se não houver resultados.
        }

        // Converte o resultado para o formato esperado (Map<String, Double>)
        Map<String, Double> finalAverages = new HashMap<>();
        rawResult.forEach((key, value) -> {
            if (value instanceof Number) {
                finalAverages.put(key, ((Number) value).doubleValue());
            }
        });

        return finalAverages;
    }
}
