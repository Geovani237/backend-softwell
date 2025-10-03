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

    // Constante para o período de cooldown: 30 dias em segundos.
    // 30 dias * 24 horas * 60 minutos * 60 segundos = 2.592.000 segundos
    private static final long COOLDOWN_SECONDS = 30 * 24 * 60 * 60;

    @Autowired
    private UserChoiceRepository userChoiceRepository;

    // Métodos Auxiliares para o Cálculo do Cooldown
    private long calculateRemainingSeconds(LocalDateTime lastChoiceData) {
        // Data/Hora que o próximo voto é permitido (data do último voto + 30 dias em segundos)
        LocalDateTime nextAllowedTime = lastChoiceData.plusSeconds(COOLDOWN_SECONDS);
        LocalDateTime now = LocalDateTime.now();

        // Se a data de permissão ainda não passou, calcula os SEGUNDOS restantes
        if (now.isBefore(nextAllowedTime)) {
            return ChronoUnit.SECONDS.between(now, nextAllowedTime);
        }
        return 0; // Cooldown terminou
    }


    public UserChoice saveChoice(String userId, String activityId) {
        Optional<UserChoice> lastChoiceOption = userChoiceRepository.findTopByUserIdOrderBySelectedDataDesc(userId);

        if (lastChoiceOption.isPresent()){
            UserChoice lastChoice = lastChoiceOption.get();
            // CHAVE: Usar segundos para precisão
            long remainingSeconds = calculateRemainingSeconds(lastChoice.getSelectedData());

            if (remainingSeconds > 0) {
                // Converte os segundos restantes para uma mensagem detalhada (Dias, Horas, Minutos e Segundos)
                long totalMinutes = remainingSeconds / 60;
                long finalRemainingSeconds = remainingSeconds % 60;

                long totalHours = totalMinutes / 60;
                long finalRemainingMinutes = totalMinutes % 60;

                long remainingDays = totalHours / 24;
                long finalRemainingHours = totalHours % 24;

                String message = String.format("Você só pode fazer uma nova escolha daqui a %d dias, %d horas, %d minutos e %d segundos.",
                        remainingDays, finalRemainingHours, finalRemainingMinutes, finalRemainingSeconds);
                throw new CooldownException(message); // Lança a exceção
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
        // CHAVE: Usar segundos
        long remainingSeconds = calculateRemainingSeconds(lastChoice.getSelectedData());

        if (remainingSeconds > 0) {
            // Retorna os dias restantes (arredondado para cima) para o DTO de status
            long remainingDays = (remainingSeconds / (24 * 60 * 60)) + 1;
            return new ChoiceStatusResponseDTO(false, remainingDays);
        }

        return new ChoiceStatusResponseDTO(true, 0);
    }
}