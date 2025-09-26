package com.example.softwell.service;

import com.example.softwell.model.Activity;
import com.example.softwell.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll();
    }

    public Activity saveActivity(Activity activity) {
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
}
