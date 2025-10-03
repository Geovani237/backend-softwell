package com.example.softwell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HumorStatusResponseDTO {
    private boolean canMakeChoice;
    private long daysRemaining;
}
