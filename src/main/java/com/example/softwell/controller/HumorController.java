package com.example.softwell.controller;

import com.example.softwell.model.Humor;
import com.example.softwell.repository.HumorRepository;
import com.example.softwell.repository.UserHumorResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/humores")
public class HumorController {

    @Autowired
    private HumorRepository humorRepository;

    @Autowired
    private UserHumorResponseRepository userHumorResponseRepository;

    @GetMapping
    public List<Humor> getAllHumor() {
        return humorRepository.findAll();
    }

    @PostMapping
    public Humor saveUserResponse(@RequestBody Humor humor) {
        humor.setDataResposta(LocalDateTime.now());
        // Salva a resposta do usuário na nova collection
        return userHumorResponseRepository.save(humor);
    }
}
