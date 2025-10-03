package com.example.softwell.controller;

import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.*;
import com.example.softwell.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


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

//    @Operation(summary = "Registra um voto do usuário em uma atividade")
//    @PostMapping("/vote")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserChoice registerVote(@RequestBody ActivityVoteDTO voteDto){
//        return service.registerUserVote(voteDto);
//    }
    /**
     * Endpoint unificado para registrar o voto de um usuário autenticado.
     * O app deve chamar este endpoint.
     * @param voteDto Contém o ID da atividade votada.
     * @param principal Injetado pelo Spring Security, contém os dados do usuário do token JWT.
     * @return Resposta de sucesso ou erro de cooldown.
     */
    @PostMapping("/choice")
    public ResponseEntity<?> registerUserChoice(@RequestBody ActivityVoteDTO voteDto, Principal principal) {
        try {
            // Extrai o nome de usuário (que você usa como ID) do token autenticado.
            String userId = principal.getName();

            // Chama o serviço com o ID do usuário real, ativando a lógica de cooldown correta.
            UserChoice newChoice = service.saveChoice(userId, voteDto.getActivityId());

            // Retorna uma resposta de sucesso. O DTO de retorno é o mesmo da requisição.
            return ResponseEntity.status(HttpStatus.CREATED).body(voteDto);

        } catch (CooldownException e) {
            // Se a exceção de cooldown for lançada, retorna um erro 400 (Bad Request) com a mensagem.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Tratamento para outros erros inesperados.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno: " + e.getMessage());
        }
    }

    /**
     * Endpoint para o Admin visualizar a contagem de votos.
     */
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
