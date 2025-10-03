package com.example.softwell.service;

import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.ChoiceStatusResponseDTO;
import com.example.softwell.model.UserChoice;
import com.example.softwell.repository.UserChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserChoiceService {

    private static final long COOLDOWN_SECONDS = 30 * 24 * 60 * 60;

    @Autowired
    private UserChoiceRepository userChoiceRepository;

    private long calculateRemainingSeconds(LocalDateTime lastChoiceData) {
        LocalDateTime nextAllowedTime = lastChoiceData.plusSeconds(COOLDOWN_SECONDS);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(nextAllowedTime)) {
            return ChronoUnit.SECONDS.between(now, nextAllowedTime);
        }
        return 0;
    }


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
        newChoice.setUserId(userId);
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
            long remainingDays = (remainingSeconds / (24 * 60 * 60)) + 1;
            return new ChoiceStatusResponseDTO(false, remainingDays);
        }

        return new ChoiceStatusResponseDTO(true, 0);
    }
}