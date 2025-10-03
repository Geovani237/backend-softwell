package com.example.softwell.controller;

import com.example.softwell.model.Humor;
import com.example.softwell.model.HumorRequestDTO;
import com.example.softwell.model.HumorStatusResponseDTO;
import com.example.softwell.model.UserHumorResponse;
import com.example.softwell.repository.HumorRepository;
import com.example.softwell.repository.UserHumorResponseRepository;
import com.example.softwell.service.HumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HumorController {

    @Autowired
    private HumorService service;

    @Autowired
    private HumorRepository humorRepository;

    @Autowired
    private UserHumorResponseRepository userHumorResponseRepository;

    @GetMapping("/humores")
    public List<Humor> getAllHumor() {
        return humorRepository.findAll();
    }

    @PostMapping("/humores/add")
    public Humor addHumor(@RequestBody Humor newHumor) {
        return humorRepository.save(newHumor);
    }

    @DeleteMapping("/humores/{id}")
    public void deleteHumor(@PathVariable String id) {
        humorRepository.deleteById(id);
    }

    @PostMapping("/humores/userresponse")
    public UserHumorResponse saveUserResponse(@RequestBody Humor humor) {
        UserHumorResponse userResponse = new UserHumorResponse();
        userResponse.setEstadoDeHumor(humor.getEstadoDeHumor());
        userResponse.setEmoji(humor.getEmoji());
        userResponse.setDataResposta(LocalDateTime.now());

        // Não se preocupe, a sua lógica de salvar no repositório correto (userHumorResponseRepository) está PERFEITA!
        return userHumorResponseRepository.save(userResponse);
    }

    @PostMapping("/humores/userhumor")
    public ResponseEntity<?> getUserHumorStatus(@RequestBody HumorRequestDTO humorRequestDTO) {
        try {
            UserHumorResponse response = service.saveUserHumorResponse(
                humorRequestDTO.getUserId(),
                humorRequestDTO.getEstadoDeHumor()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    @GetMapping("/humores/status/{userId}")
    public ResponseEntity<HumorStatusResponseDTO> getStatus(@PathVariable String userId){
        HumorStatusResponseDTO status = service.getHumorStatus(userId);
        return ResponseEntity.ok(status);
    }
}