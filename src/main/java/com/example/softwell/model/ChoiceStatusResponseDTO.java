package com.example.softwell.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChoiceStatusResponseDTO {
    private boolean canMakeChoice;
    private long daysRemaining;
}
