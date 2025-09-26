package com.example.softwell.controller;

import com.example.softwell.model.Activity;
import com.example.softwell.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/act")
public class ActivityController {
    @Autowired
    private ActivityService service;

    @GetMapping("/activity")
    @ResponseStatus(HttpStatus.OK)
    public List<Activity> listActivity() {
        return service.getAllActivity();
    }

    @PostMapping("/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public Activity save(@RequestBody Activity activity){
        return service.saveActivity(activity);
    }

    @DeleteMapping("/activity/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        service.deleteActivity(id);
    }

    @PutMapping("/activity")
    @ResponseStatus(HttpStatus.OK )
    public Activity update(@RequestBody Activity activity) {
        return service.updateActivity(activity);
    }


}
