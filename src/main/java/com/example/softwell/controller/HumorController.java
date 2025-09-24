package com.example.softwell.controller;

import com.example.softwell.model.Humor;
import com.example.softwell.repository.HumorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/humores")
public class HumorController {

    @Autowired
    private HumorRepository humorRepository;

    @GetMapping
    public Humor getHumor() {
        // Pega o primeiro objeto da lista retornada pelo repository
        return humorRepository.findAll().get(0);
    }
}