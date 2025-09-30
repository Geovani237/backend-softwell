package com.example.softwell.controller;

import com.example.softwell.model.Activity;
import com.example.softwell.model.ActivityCreateDTO; // NOVO: DTO de entrada para criação
import com.example.softwell.model.ActivityVoteDTO;    // NOVO: DTO de entrada para voto
import com.example.softwell.model.ActivityVoteReportDTO;
import com.example.softwell.model.UserChoice;        // NOVO: Retorna o registro de voto
import com.example.softwell.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
}
