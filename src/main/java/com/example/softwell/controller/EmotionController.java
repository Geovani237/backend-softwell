package com.example.softwell.controller;

import com.example.softwell.model.Emotion;
import com.example.softwell.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmotionController {

    @Autowired
    private EmotionService service;

    //Somente USER
    @GetMapping("emotions")
    //@PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public List<Emotion> listEmotion(){
        return service.findAll();
    }
    //----------------


    //Somente ADMIN
    @PostMapping("/emotions")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Emotion save(@RequestBody String emotion){
        return service.saveEmotion(emotion);
    }

    @DeleteMapping("/emotions/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.deleteEmotion(id);
    }

    @PutMapping("/emotions")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Emotion update(@RequestBody Emotion emotion) {
        return service.updateEmotion(emotion);
    }

}
