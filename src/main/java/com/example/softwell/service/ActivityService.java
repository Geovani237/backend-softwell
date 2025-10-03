package com.example.softwell.service;

import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.*;
import com.example.softwell.repository.ActivityRepository;
import com.example.softwell.repository.UserChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    // Cooldown de 30 dias em segundos (30 dias * 24 horas * 60 minutos * 60 segundos)
    private static final long COOLDOWN_SECONDS = 30 * 24 * 60 * 60;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserChoiceRepository userChoiceRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll();
    }

    public Activity saveActivityFromDto(ActivityCreateDTO activityDto) {
        Activity activity = new Activity();
        activity.setActivity(activityDto.getActivity());
        activity.setId(null);
        activity.setDate(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public Activity saveActivity(Activity activity) {
        activity.setId(null);
        activity.setDate(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public void deleteActivity(String id) {
        activityRepository.deleteById(id);
    }

    public Activity updateActivity(Activity activity) {
        Optional<Activity> optionalActivity = activityRepository.findById(activity.getId());

        if (optionalActivity.isPresent()){
            return activityRepository.save(activity);
        } else {
            throw new RuntimeException("Atividade não encontrada");
        }
    }

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

    private long calculateRemainingSeconds(LocalDateTime lastChoiceData) {
        LocalDateTime nextAllowedTime = lastChoiceData.plusSeconds(COOLDOWN_SECONDS);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(nextAllowedTime)) {
            return ChronoUnit.SECONDS.between(now, nextAllowedTime);
        }
        return 0; // Cooldown terminou
    }

    /**
     * Salva a escolha (voto) de um usuário, aplicando a regra de cooldown de 30 dias.
     * Este método agora é a única fonte para registrar votos.
     * @param userId O ID do usuário vindo do token de autenticação.
     * @param activityId O ID da atividade que está sendo votada.
     * @return A entidade UserChoice salva.
     * @throws CooldownException se o usuário tentar votar antes do período de 30 dias.
     */
    public UserChoice saveChoice(String userId, String activityId) {
        Optional<UserChoice> lastChoiceOption = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (lastChoiceOption.isPresent()){
            UserChoice lastChoice = lastChoiceOption.get();
            long remainingSeconds = calculateRemainingSeconds(lastChoice.getSelectedData());

            if (remainingSeconds > 0) {
                long totalMinutes = remainingSeconds / 60;
                long finalRemainingSeconds = remainingSeconds % 60;
                long totalHours = totalMinutes / 60;
                long finalRemainingMinutes = totalMinutes % 60;
                long remainingDays = totalHours / 24;
                long finalRemainingHours = totalHours % 24;

                String message = String.format("Você só pode fazer uma nova escolha daqui a %d dias, %d horas, %d minutos e %d segundos.",
                        remainingDays, finalRemainingHours, finalRemainingMinutes, finalRemainingSeconds);
                throw new CooldownException(message);
            }
        }

        UserChoice newChoice = new UserChoice();
        newChoice.setUserId(userId); // Associa o voto ao usuário correto.
        newChoice.setActivityId(activityId);
        newChoice.setSelectedData(LocalDateTime.now());

        return userChoiceRepository.save(newChoice);
    }

    public ChoiceStatusResponseDTO getChoiceStatus(String userId) {
        Optional<UserChoice> lastChoiceOptional = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (!lastChoiceOptional.isPresent()){
            return new ChoiceStatusResponseDTO(true, 0);
        }

        UserChoice lastChoice = lastChoiceOptional.get();
        long remainingSeconds = calculateRemainingSeconds(lastChoice.getSelectedData());

        if (remainingSeconds > 0) {
            // Adiciona 1 para arredondar para cima (ex: 29.5 dias restantes se torna 30 dias)
            long remainingDays = (remainingSeconds / (24 * 60 * 60)) + 1;
            return new ChoiceStatusResponseDTO(false, remainingDays);
        }

        return new ChoiceStatusResponseDTO(true, 0);
    }

    // O método registerUserVote(ActivityVoteDTO voteDto) foi removido pois estava incorreto e não é mais necessário.
}
