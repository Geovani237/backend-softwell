package com.example.softwell.util;

import java.util.HashMap; // NOVO IMPORT NECESSÁRIO
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression; // MANTIDO

public class MongoAggregationUtils {

    // 🚨 CORREÇÃO DO ERRO 'Map.of' - Usando HashMap e bloco estático
    private static final Map<String, Integer> RATING_MAP;

    static {
        RATING_MAP = new HashMap<>();
        RATING_MAP.put("Não", 1);
        RATING_MAP.put("Nunca", 1);
        RATING_MAP.put("Muito Leve", 1);
        RATING_MAP.put("Raramente", 2);
        RATING_MAP.put("Leve", 2);
        RATING_MAP.put("Às vezes", 3);
        RATING_MAP.put("Média", 3);
        RATING_MAP.put("Frequentemente", 4);
        RATING_MAP.put("Alta", 4);
        RATING_MAP.put("Sempre", 5);
        RATING_MAP.put("Muito Alta", 5);
        // Note: Se o seu Java for 11+, você pode usar Map.copyOf(RATING_MAP) para torná-lo imutável,
        // mas o HashMap simples funciona para a criação estática.
    }

    /**
     * Gera a expressão de agregação $switch para converter valores de String (do DTO) para Int.
     *
     * @param fieldPath O caminho do campo no MongoDB (ex: "$workload.workloadAssessment").
     * @return Uma expressão de agregação que retorna o valor numérico (1 a 5).
     */
    // O tipo de retorno AggregationExpression é o mais robusto e está correto.
    public static AggregationExpression convertStringToInt(String fieldPath) {

        List<Document> branches = RATING_MAP.entrySet().stream()
                .map(entry -> new Document("case", new Document("$eq", List.of(fieldPath, entry.getKey())))
                        .append("then", entry.getValue()))
                .collect(Collectors.toList());

        // Documento padrão para $switch
        Document switchDoc = new Document("$switch", new Document("branches", branches)
                .append("default", 3));

        return new AggregationExpression() {
            @Override
            public Document toDocument(AggregationOperationContext context) {
                return switchDoc;
            }
        };
    }

    public static String convertStringToIntForAvg(String s) {
        return s;
    }
}