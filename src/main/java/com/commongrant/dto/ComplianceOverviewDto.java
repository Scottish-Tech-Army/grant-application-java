package com.commongrant.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ComplianceOverviewDto {
    private int overallScore;
    private int documentationScore;
    private int financialScore;
    private int milestonesScore;
    private int reportsScore;
    private List<DeadlineDto> upcomingDeadlines;
}

