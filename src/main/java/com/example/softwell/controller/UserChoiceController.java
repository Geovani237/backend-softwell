package com.example.softwell.controller;

import com.example.softwell.dto.ChoiceRequestDTO;
import com.example.softwell.dto.ChoiceStatusResponseDTO;
import com.example.softwell.model.UserChoice;
import com.example.softwell.service.UserChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/choices")
public class UserChoiceController {

    @Autowired
    private UserChoiceService userChoiceService;

    @PostMapping
    public ResponseEntity<UserChoice> submitChoice(@RequestBody ChoiceRequestDTO choiceRequestDTO){
        UserChoice newChoice = userChoiceService.saveChoice(
                choiceRequestDTO.getUserId(),
                choiceRequestDTO.getActivityId()
        );

        return new ResponseEntity<>(newChoice, HttpStatus.CREATED);
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<ChoiceStatusResponseDTO> getStatus(@PathVariable String userId){
        ChoiceStatusResponseDTO status = userChoiceService.getChoiceStatus(userId);
        return ResponseEntity.ok(status);
    }
}
