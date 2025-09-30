package com.example.softwell.service;

import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.ChoiceStatusResponseDTO;
import com.example.softwell.model.UserChoice;
import com.example.softwell.repository.UserChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserChoiceService {
    @Autowired
    private UserChoiceRepository userChoiceRepository;


    public UserChoice saveChoice(String userId, String activityId) {
        Optional<UserChoice> lastChoiceOption = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (lastChoiceOption.isPresent()){
            UserChoice lastChoice = lastChoiceOption.get();
            LocalDate lastChoiseData = lastChoice.getSelectedData().toLocalDate();
            LocalDate today = LocalDate.now();

            long daysDiferrence = ChronoUnit.DAYS.between(lastChoiseData, today);

            if (daysDiferrence < 30) {
                long daysRemaining = 30 - daysDiferrence;
                throw new CooldownException("Você só pode fazer uma nova escolha daqui a " + daysRemaining + " dias.");
            }
        }

        UserChoice newChoice = new UserChoice();
        newChoice.setUserId(userId);
        // Use activityId
        newChoice.setActivityId(activityId);
        newChoice.setSelectedData(LocalDateTime.now());

//        UserChoice newChoice = new UserChoice();
//        newChoice.setUserId(userId);
//        newChoice.setSelectedOption(option);
//        newChoice.setSelectedData(LocalDateTime.now());

        return userChoiceRepository.save(newChoice);
    }

    public ChoiceStatusResponseDTO getChoiceStatus(String userId) {
        Optional<UserChoice> lastChoiceOptional = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (!lastChoiceOptional.isPresent()){
            return new ChoiceStatusResponseDTO(true, 0);
        }

        UserChoice lastChoice = lastChoiceOptional.get();
        LocalDate lastChoiceDate = lastChoice.getSelectedData().toLocalDate();
        LocalDate today = LocalDate.now();

        long daysDifference = ChronoUnit.DAYS.between(lastChoiceDate, today);

        if (daysDifference < 30) {
            long daysRemaining = 30 - daysDifference;
            return new ChoiceStatusResponseDTO(false, daysRemaining);
        }

        return new ChoiceStatusResponseDTO(true, 0);
    }
}
