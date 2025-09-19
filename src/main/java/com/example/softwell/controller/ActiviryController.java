package com.example.softwell.controller;

import com.example.softwell.model.Activity;
import com.example.softwell.service.ActiviryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/act")
public class ActiviryController {
    @Autowired
    private ActiviryService service;

    @GetMapping("/activiry")
    @ResponseStatus(HttpStatus.OK)
    public List<Activity> listActiviry() {
        return service.getAllActiviry();
    }

    @PostMapping("/activiry")
    @ResponseStatus(HttpStatus.CREATED)
    public Activity save(@RequestBody Activity activity){
        return service.saveActiviry(activity);
    }

    @DeleteMapping("/activiry/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        service.deleteActiviry(id);
    }

    @PutMapping("/activiry")
    @ResponseStatus(HttpStatus.OK )
    public Activity update(@RequestBody Activity activity) {
        return service.updateActiviry(activity);
    }


}
