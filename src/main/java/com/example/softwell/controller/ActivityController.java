package com.example.softwell.controller;

import com.example.softwell.model.*;
import com.example.softwell.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/act")
public class ActivityController {
    @Autowired
    private ActivityService service;

    @GetMapping("/activity")
    @ResponseStatus(HttpStatus.OK)
    public List<Activity> listActivity() {
        return service.getAllActivity();
    }

    /**
     * CORRIGIDO: Recebe ActivityCreateDTO (apenas 'activity') para evitar erro 400.
     * Mapeia para a entidade Activity no Service.
     */
    @PostMapping("/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public Activity save(@RequestBody ActivityCreateDTO activityDto){
        // Chama o serviço com o DTO de entrada
        return service.saveActivityFromDto(activityDto);
    }

    @DeleteMapping("/activity/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        service.deleteActivity(id);
    }

    @PutMapping("/activity")
    @ResponseStatus(HttpStatus.OK )
    public Activity update(@RequestBody Activity activity) {
        return service.updateActivity(activity);
    }

    /**
     * Endpoint para registrar o voto do usuário.
     */
    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public UserChoice registerVote(@RequestBody ActivityVoteDTO voteDto){
        return service.registerUserVote(voteDto);
    }

    /**
     * Endpoint para o Admin visualizar a contagem de votos.
     */
    @GetMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityVoteReportDTO> getVoteReport() {
        return service.generateVoteReport();
    }


    @PostMapping("/choice")
    public ResponseEntity<?> submitChoice(@RequestBody ChoiceRequestDTO choiceRequestDTO){
        try {
            UserChoice newChoice = service.saveChoice(
                    choiceRequestDTO.getUserId(),
                    choiceRequestDTO.getActivityId()
            );
            return new ResponseEntity<>(newChoice, HttpStatus.CREATED);
        } catch (Exception ex) {
            // Retorna 400 Bad Request com a mensagem de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }



    @GetMapping("/status/{userId}")
    public ResponseEntity<ChoiceStatusResponseDTO> getStatus(@PathVariable String userId){
        ChoiceStatusResponseDTO status = service.getChoiceStatus(userId);
        return ResponseEntity.ok(status);
    }
}
