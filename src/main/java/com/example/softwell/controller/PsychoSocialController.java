package com.example.softwell.controller;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.service.PsychoSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/psychosocial")
@RequiredArgsConstructor // Injeta o serviço via construtor
public class PsychoSocialController {

    private final PsychoSocialService service;

    // POST: /api/psychosocial/submit
    // Recebe a resposta completa do Android
    @PostMapping("/submit")
    public ResponseEntity<PsychoSocialAnswer> submitAnswers(@RequestBody PsychoSocialAnswer answer) {
        // Validação básica para garantir o ID
        if (answer.getUserId() == null || answer.getUserId().trim().isEmpty()) {
            return new ResponseEntity("User ID is required.", HttpStatus.BAD_REQUEST);
        }
        PsychoSocialAnswer saved = service.saveAnswer(answer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // GET: /api/psychosocial/user/{userId}
    // Para ver todas as respostas que um usuário específico já enviou
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PsychoSocialAnswer>> getUserHistory(@PathVariable String userId) {
        List<PsychoSocialAnswer> history = service.getAnswersByUserId(userId);
        return ResponseEntity.ok(history);
    }

    // GET: /api/psychosocial/analysis/leadership/average
    // Endpoint para buscar o resultado das análises
    @GetMapping("/analysis/leadership/average")
    public ResponseEntity<Map<String, Double>> getLeadershipAverage() {
        Map<String, Double> averages = service.calculateLeadershipAverage();
        return ResponseEntity.ok(averages);
    }
}