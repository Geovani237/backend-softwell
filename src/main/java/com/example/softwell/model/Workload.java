package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Workload {
    private String workloadAssessment; // Ex: "Muito Alta"
    private String qualityOfLifeImpact; // Ex: "Sempre"
    private String extraHours; // Ex: "Frequentemente"
}