package com.example.softwell.controller;

import com.example.softwell.model.Humor;
import com.example.softwell.model.UserHumorResponse;
import com.example.softwell.repository.HumorRepository;
import com.example.softwell.repository.UserHumorResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HumorController {

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
}