package com.example.softwell.service;

import com.example.softwell.model.Activity;
import com.example.softwell.repository.ActiviryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActiviryService {

    @Autowired
    private ActiviryRepository activiryRepository;

    public List<Activity> getAllActiviry() {
        return activiryRepository.findAll();
    }

    public Activity saveActiviry(Activity activity) {
        activity.setDate(LocalDateTime.now());
        return activiryRepository.save(activity);
    }

    public void deleteActiviry(String id) {
        activiryRepository.deleteById(id);
    }

    public Activity updateActiviry(Activity activity) {
        Optional<Activity> OptinalActiviry = activiryRepository.findById(activity.getId());

        if (OptinalActiviry.isPresent()){
            return activiryRepository.save(activity);
        } else {
            throw new RuntimeException("Atividade não encontrada");
        }
    }
}
