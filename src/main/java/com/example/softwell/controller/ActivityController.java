package com.example.softwell.controller;

import com.example.softwell.model.*;
import com.example.softwell.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/act")
@Tag(name = "Atividades", description = "Endpoints para gerenciamento de atividades")
public class ActivityController {
    @Autowired
    private ActivityService service;

    @Operation(summary = "Lista todas as atividades")
    @GetMapping("/activity")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK )
    public Activity update(@RequestBody Activity activity) {
        return service.updateActivity(activity);
    }

    @Operation(summary = "Registra um voto do usuário em uma atividade")
    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public UserChoice registerVote(@RequestBody ActivityVoteDTO voteDto){
        return service.registerUserVote(voteDto);
    }

    /**
     * Endpoint para o Admin visualizar a contagem de votos.
     */
    @Operation(summary = "Relatório de votos das atividades (admin)")
    @GetMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityVoteReportDTO> getVoteReport() {
        return service.generateVoteReport();
    }

    @Operation(summary = "Submete a escolha de atividade do usuário")
    @PostMapping("/choice")
    public ResponseEntity<?> submitChoice(@RequestBody ChoiceRequestDTO choiceRequestDTO){
        try {
            UserChoice newChoice = service.saveChoice(
                    choiceRequestDTO.getUserId(),
                    choiceRequestDTO.getActivityId()
            );
            return new ResponseEntity<>(newChoice, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Consulta o status da escolha do usuário")
    @GetMapping("/status/{userId}")
    public ResponseEntity<ChoiceStatusResponseDTO> getStatus(@PathVariable String userId){
        ChoiceStatusResponseDTO status = service.getChoiceStatus(userId);
        return ResponseEntity.ok(status);
    }
}
