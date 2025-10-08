package com.example.softwell.controller;

import com.example.softwell.dto.ActivityCreateDTO;
import com.example.softwell.dto.ActivityVoteDTO;
import com.example.softwell.dto.ActivityVoteReportDTO;
import com.example.softwell.dto.ChoiceStatusResponseDTO;
import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.*;
import com.example.softwell.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/act")
@Tag(name = "Atividades", description = "Endpoints para gerenciamento de atividades")
public class ActivityController {

    @Autowired
    private ActivityService service;

    @Operation(summary = "Lista todas as atividades")
    @GetMapping("/activity")
    public List<Activity> listActivity() {
        return service.getAllActivity();
    }

    @Operation(summary = "Cria uma nova atividade")
    @PostMapping("/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public Activity save(@RequestBody ActivityCreateDTO activityDto){
        return service.saveActivityFromDto(activityDto);
    }

    @Operation(summary = "Deleta uma atividade pelo ID")
    @DeleteMapping("/activity/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        service.deleteActivity(id);
    }

    @Operation(summary = "Atualiza uma atividade")
    @PutMapping("/activity")
    public Activity update(@RequestBody Activity activity) {
        return service.updateActivity(activity);
    }

    @Operation(summary = "Submete a escolha de atividade do usuário")
    @PostMapping("/choice")
    public ResponseEntity<?> registerUserChoice(@RequestBody ActivityVoteDTO voteDto, Principal principal) {
        try {
            String userId = principal.getName();

            UserChoice newChoice = service.saveChoice(userId, voteDto.getActivityId());

            return ResponseEntity.status(HttpStatus.CREATED).body(voteDto);

        } catch (CooldownException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno: " + e.getMessage());
        }
    }

    @Operation(summary = "Relatório de votos das atividades (admin)")
    @GetMapping("/report")
    public List<ActivityVoteReportDTO> getVoteReport() {
        return service.generateVoteReport();
    }

    @GetMapping("/status")
    public ResponseEntity<ChoiceStatusResponseDTO> getChoiceStatus(Principal principal){
        String userId = principal.getName();
        ChoiceStatusResponseDTO status = service.getChoiceStatus(userId);
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Consulta o status da escolha do usuário")
    @GetMapping("/status/{userId}")
    public ResponseEntity<ChoiceStatusResponseDTO> getStatus(@PathVariable String userId){
        ChoiceStatusResponseDTO status = service.getChoiceStatus(userId);
        return ResponseEntity.ok(status);
    }


}
