package com.example.softwell.service;

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


    public void saveChoice(String userId, String option) {
        Optional<UserChoice> lastChoiceOption = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (lastChoiceOption.isPresent()){
            UserChoice lastChoice = lastChoiceOption.get();
            LocalDate lastChoiseData = lastChoice.getSelectedData().toLocalDate();
            LocalDate today = LocalDate.now();

            long daysDiferrence = ChronoUnit.DAYS.between(lastChoiseData, today);

            if (daysDiferrence < 30) {
                throw new RuntimeException("Você só pode fazer uma nova escolha depois de 30 dias.");
            }
        }

        UserChoice newChoice = new UserChoice();
        newChoice.setUserId(userId);
        newChoice.setSelectedOption(option);
        newChoice.setSelectedData(LocalDateTime.now());

        userChoiceRepository.save(newChoice);
    }
}
