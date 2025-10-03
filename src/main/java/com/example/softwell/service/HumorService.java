package com.example.softwell.service;

import com.example.softwell.exception.CooldownException;
import com.example.softwell.model.HumorStatusResponseDTO;
import com.example.softwell.model.UserHumorResponse;
import com.example.softwell.repository.UserHumorResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class HumorService {

    private static final long COOLDOWN_PERIOD_SECONDS = 24 * 60 * 60; // 24 hours in milliseconds


    @Autowired
    private UserHumorResponseRepository userHumorResponseRepository;

    private long calculateRemainsSeconds(LocalDateTime lastChoiceData){
        LocalDateTime nextAllowedTime = lastChoiceData.plusSeconds(COOLDOWN_PERIOD_SECONDS);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(nextAllowedTime)){
            return ChronoUnit.SECONDS.between(now, nextAllowedTime);
        }
        return 0;
    }

    public UserHumorResponse saveUserHumorResponse(String userId, String estadoDeHumor){
        Optional<UserHumorResponse> lastChoiceOpt = userHumorResponseRepository.findTopByUserIdOrderByDataRespostaDesc(userId);

        if (lastChoiceOpt.isPresent()){
             UserHumorResponse lastChoice = lastChoiceOpt.get();
             long remainsSeconds = calculateRemainsSeconds(lastChoice.getDataResposta());
             if (remainsSeconds > 0){
                 long remainsHours = remainsSeconds / 3600;
                 long remainsMinutes = (remainsSeconds % 3600) / 60;
                 long remainsSecs = remainsSeconds % 60;
                 String message = String.format("Você só pode fazer uma nova escolha daqui a %d horas, %d minutos e %d segundos.",remainsHours, remainsMinutes, remainsSecs);
                 throw new CooldownException(message);             }
        }
        UserHumorResponse newResponse = new UserHumorResponse();
        newResponse.setUserId(userId);
        newResponse.setEstadoDeHumor(estadoDeHumor);
        newResponse.setDataResposta(LocalDateTime.now());
        // Aqui você pode definir o emoji com base no estadoDeHumor, se necessário.
        return userHumorResponseRepository.save(newResponse);
    }

    public HumorStatusResponseDTO getHumorStatus(String userId){
        Optional<UserHumorResponse> lastChoiceOpt = userHumorResponseRepository.findTopByUserIdOrderByDataRespostaDesc(userId);
        if (!lastChoiceOpt.isPresent()){
            return new HumorStatusResponseDTO(true,0);
        }
        UserHumorResponse lastChoice = lastChoiceOpt.get();
        long remainsSeconds = calculateRemainsSeconds(lastChoice.getDataResposta());
        if (remainsSeconds > 0){
             return new HumorStatusResponseDTO(false,remainsSeconds);
        }
        return new HumorStatusResponseDTO(true, 0);
    }
}
