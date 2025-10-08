package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Workload {
    private String workloadAssessment;
    private String qualityOfLifeImpact;
    private String extraHours;
}