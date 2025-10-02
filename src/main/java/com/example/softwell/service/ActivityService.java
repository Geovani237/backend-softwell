package com.example.softwell.service;

import com.example.softwell.model.Activity;
import com.example.softwell.model.ActivityCreateDTO;
import com.example.softwell.model.ActivityVoteDTO;
import com.example.softwell.model.ActivityVoteReportDTO;
import com.example.softwell.model.UserChoice;
import com.example.softwell.repository.ActivityRepository;
import com.example.softwell.repository.UserChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserChoiceRepository userChoiceRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll();
    }

    /**
     * NOVO MÉTODO DE SALVAMENTO: Recebe o DTO limpo para evitar o erro 400 e
     * mapeia para a Entidade para salvar.
     */
    public Activity saveActivityFromDto(ActivityCreateDTO activityDto) {
        Activity activity = new Activity();
        activity.setActivity(activityDto.getActivity());

        // Garante a criação de um novo ID e preenche a data
        activity.setId(null);
        activity.setDate(LocalDateTime.now());

        return activityRepository.save(activity);
    }

    public Activity saveActivity(Activity activity) {
        // Método de fallback/CRUD simples, ainda precisa ser protegido
        activity.setId(null);
        activity.setDate(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public void deleteActivity(String id) {
        activityRepository.deleteById(id);
    }

    public Activity updateActivity(Activity activity) {
        Optional<Activity> OptinalActivity = activityRepository.findById(activity.getId());

        if (OptinalActivity.isPresent()){
            return activityRepository.save(activity);
        } else {
            throw new RuntimeException("Atividade não encontrada");
        }
    }

    /**
     * NOVO MÉTODO: Registra o voto do usuário na collection 'userChoice'.
     */
    public UserChoice registerUserVote(ActivityVoteDTO voteDto) {
        UserChoice voteRecord = new UserChoice();

        voteRecord.setActivityId(voteDto.getActivityId());

        // **LÓGICA DE USUÁRIO E DATA**
        voteRecord.setUserId("DEFAULT_USER_ID"); // Implemente a autenticação real
        voteRecord.setSelectedData(LocalDateTime.now());

        return userChoiceRepository.save(voteRecord);
    }

    /**
     * Gera o relatório de votação combinando Atividades e Contagem de Votos.
     */
    public List<ActivityVoteReportDTO> generateVoteReport() {
        List<Activity> activities = activityRepository.findAll();

        return activities.stream().map(activity -> {
            long voteCount = userChoiceRepository.countByActivityId(activity.getId());

            return new ActivityVoteReportDTO(
                    activity.getId(),
                    activity.getActivity(),
                    voteCount
            );
        }).collect(Collectors.toList());
    }
}