package com.example.softwell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVoteReportDTO {
    private String activityId;
    private String activityName;
    private Long voteCount;
}