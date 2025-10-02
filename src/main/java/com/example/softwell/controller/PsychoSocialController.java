package com.example.softwell.controller;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.service.PsychoSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // ✅ Importação necessária
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/psychosocial")
@RequiredArgsConstructor
public class PsychoSocialController {

    private final PsychoSocialService service;

    /**
     * Endpoint para submissão do questionário pelo usuário.
     * @return 201 Created com a resposta salva.
     */
    @PostMapping("/submit")
    public ResponseEntity<PsychoSocialAnswer> submitAnswers(@RequestBody PsychoSocialAnswer answer) {
        PsychoSocialAnswer savedAnswer = service.saveAnswer(answer);
        return ResponseEntity.status(201).body(savedAnswer);
    }

    /**
     * Endpoint para o usuário ver seu histórico individual (identificado).
     * @param userId O ID do usuário (enviado pelo Android).
     * @return Lista de todas as respostas daquele usuário.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PsychoSocialAnswer>> getUserHistory(@PathVariable String userId) {
        List<PsychoSocialAnswer> history = service.getAnswersByUserId(userId);
        if (history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }


    /**
     * Endpoint para Administradores:
     * Retorna as 5 médias temáticas calculadas de forma sigilosa.
     * @return Map com as 5 médias (avgWorkload, avgWarningSigns, etc.).
     */
    @GetMapping("/analysis/averages")
    public ResponseEntity<Map<String, Double>> getOverallAverages() {
        // NOTE: Este método está no service, mas não foi implementado no escopo atual.
        Map<String, Double> averages = service.calculateThematicAverages();

        if (averages == null || averages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(averages);
    }

    // --- ENDPOINT PARA A TELA DE GRÁFICOS PESSOAIS ---

    /**
     * Endpoint para Usuários:
     * Retorna as 5 médias temáticas APENAS do último questionário
     * respondido pelo usuário autenticado (extraído do JWT).
     *
     * @param authentication Objeto injetado pelo Spring Security, contendo o usuário logado.
     * @return Map com as 5 médias temáticas (String -> Double).
     */
    @GetMapping("/analysis/latest-averages")
    public ResponseEntity<Map<String, Double>> getLatestAverages(Authentication authentication) {

        // ✅ CORREÇÃO: Pega o ID/Username do usuário autenticado no token JWT.
        String userId = authentication.getName();

        Map<String, Double> averages = service.calculateLatestThematicAverages(userId);

        if (averages == null || averages.isEmpty()) {
            // Retorna 204 No Content se não houver dados para o usuário
            return ResponseEntity.noContent().build();
        }

        // Retorna as médias do último questionário (200 OK)
        return ResponseEntity.ok(averages);
    }
}