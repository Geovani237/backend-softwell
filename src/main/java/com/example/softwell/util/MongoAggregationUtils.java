package com.example.softwell.util;

import java.util.HashMap; // NOVO IMPORT NECESSÁRIO
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression; // MANTIDO

public class MongoAggregationUtils {

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

    }

    public static AggregationExpression convertStringToInt(String fieldPath) {

        List<Document> branches = RATING_MAP.entrySet().stream()
                .map(entry -> new Document("case", new Document("$eq", List.of(fieldPath, entry.getKey())))
                        .append("then", entry.getValue()))
                .collect(Collectors.toList());

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