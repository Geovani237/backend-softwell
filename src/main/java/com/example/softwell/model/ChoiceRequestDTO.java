package com.example.softwell.model;

import lombok.Data;

@Data
public class ChoiceRequestDTO {
    private String userId;
    // Mude de selectedOption (Nome) para activityId (ID)
    private String activityId;
}
