package com.example.softwell.controller;

import com.example.softwell.model.Humor;
import com.example.softwell.model.HumorRequestDTO;
import com.example.softwell.model.HumorStatusResponseDTO;
import com.example.softwell.model.UserHumorResponse;
import com.example.softwell.repository.HumorRepository;
import com.example.softwell.repository.UserHumorResponseRepository;
import com.example.softwell.service.HumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
// ✅ 1. CORREÇÃO DEFINITIVA: O prefixo base para TUDO neste controller é "/api/humores".
@RequestMapping("/api/humores")
public class HumorController {

    @Autowired
    private HumorService service;

    @Autowired
    private HumorRepository humorRepository;

    @Autowired
    private UserHumorResponseRepository userHumorResponseRepository;

    // Caminho final: GET /api/humores
    @GetMapping
    public List<Humor> getAllHumor() {
        return humorRepository.findAll();
    }

    // Caminho final: POST /api/humores/add
    @PostMapping("/add")
    public Humor addHumor(@RequestBody Humor newHumor) {
        return humorRepository.save(newHumor);
    }

    // Caminho final: DELETE /api/humores/{id}
    @DeleteMapping("/{id}")
    public void deleteHumor(@PathVariable String id) {
        humorRepository.deleteById(id);
    }

    // (Este endpoint parece ser uma versão antiga para salvar resposta. Pode ser removido se não for usado.)
    // Caminho final: POST /api/humores/userresponse
    @PostMapping("/userresponse")
    public UserHumorResponse saveUserResponse(@RequestBody Humor humor) {
        UserHumorResponse userResponse = new UserHumorResponse();
        userResponse.setEstadoDeHumor(humor.getEstadoDeHumor());
        userResponse.setEmoji(humor.getEmoji());
        userResponse.setDataResposta(LocalDateTime.now());
        return userHumorResponseRepository.save(userResponse);
    }

    // Caminho final: POST /api/humores/userhumor
    @PostMapping("/userhumor")
    public ResponseEntity<?> saveUserHumorChoice(@RequestBody HumorRequestDTO humorRequestDTO) {
        try {
            UserHumorResponse response = service.saveUserHumorResponse(
                    humorRequestDTO.getUserId(),
                    humorRequestDTO.getEstadoDeHumor(),
                    humorRequestDTO.getEmoji()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Caminho final: GET /api/humores/status/{userId}
    @GetMapping("/status/{userId}")
    public ResponseEntity<HumorStatusResponseDTO> getStatus(@PathVariable String userId){
        HumorStatusResponseDTO status = service.getHumorStatus(userId);
        return ResponseEntity.ok(status);
    }

    // ✅ 2. O ENDPOINT DE HISTÓRICO AGORA FUNCIONA.
    // Caminho final: GET /api/humores/history/by-date?date=YYYY-MM-DD
    @GetMapping("/history/by-date")
    public ResponseEntity<List<UserHumorResponse>> getHumorHistoryByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<UserHumorResponse> responses = userHumorResponseRepository.findByDataRespostaBetween(startOfDay, endOfDay);

        // Este LOG é crucial para depuração no console do seu backend.
        System.out.println("[BACKEND LOG] Buscando humores para data: " + date + ". Encontrados: " + responses.size() + " registros.");

        return ResponseEntity.ok(responses);
    }
}