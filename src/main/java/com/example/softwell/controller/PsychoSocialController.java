package com.example.softwell.controller;

import com.example.softwell.model.PsychoSocialAnswer;
import com.example.softwell.service.PsychoSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; // ✅ 1. Importação para a data
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // ✅ 2. Importação para a data
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/psychosocial")
@RequiredArgsConstructor
public class PsychoSocialController {

    private final PsychoSocialService service;

    @PostMapping("/submit")
    public ResponseEntity<PsychoSocialAnswer> submitAnswers(@RequestBody PsychoSocialAnswer answer) {
        PsychoSocialAnswer savedAnswer = service.saveAnswer(answer);
        return ResponseEntity.status(201).body(savedAnswer);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PsychoSocialAnswer>> getUserHistory(@PathVariable String userId) {
        List<PsychoSocialAnswer> history = service.getAnswersByUserId(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/analysis/latest-averages")
    public ResponseEntity<Map<String, Double>> getLatestAverages(Authentication authentication) {
        String userId = authentication.getName();
        Map<String, Double> averages = service.calculateLatestThematicAverages(userId);
        if (averages == null || averages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(averages);
    }

    @GetMapping("/analysis/by-date/{date}")
    public ResponseEntity<Map<String, Double>> getAveragesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Map<String, Double> averages = service.calculateAveragesByDate(date);

        if (averages == null || averages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(averages);
    }
}
