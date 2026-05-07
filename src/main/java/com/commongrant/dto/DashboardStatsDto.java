package com.commongrant.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsDto {
    private long activeApplications;
    private BigDecimal fundingRequested;
    private BigDecimal fundingApproved;
    private long approvedCount;
    private int complianceScore;
    private List<ApplicationSummaryDto> recentApplications;
}

