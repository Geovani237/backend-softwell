package com.example.softwell.service;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.repository.PsychoSocialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor // Usa o Lombok para injetar dependências no construtor (melhor prática)
public class PsychoSocialService {

    private final PsychoSocialRepository repository;
    private final MongoTemplate mongoTemplate;

    // 1. Salva a resposta do Android
    public PsychoSocialAnswer saveAnswer(PsychoSocialAnswer answer) {
        // Você pode adicionar regras de negócio/validação aqui (ex: impedir spam de respostas)
        return repository.save(answer);
    }

    // 2. Busca o histórico do usuário
    public List<PsychoSocialAnswer> getAnswersByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    // 3. Exemplo de Agregação (Para calcular médias)
    // Aqui você faria as 5 funções de cálculo de média (uma para cada setor)
    public Map<String, Double> calculateLeadershipAverage() {

        // Exemplo simplificado de como seria o cálculo da média
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group()
                        .avg("leadershipRelation.leaderCaresWellbeing").as("avgCaresWellbeing")
                        .avg("leadershipRelation.leaderIsAvailable").as("avgIsAvailable")
                // ... adicione as outras 3 perguntas de Liderança aqui...
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                agg, "psychosocial_answers", Map.class
        );

        return (Map<String, Double>) results.getUniqueMappedResult();
    }
}