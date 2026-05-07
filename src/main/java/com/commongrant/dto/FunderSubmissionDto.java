package com.commongrant.dto;

import com.commongrant.model.GrantApplication.ApplicationStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FunderSubmissionDto {
    private Long id;
    private Long funderId;
    private String funderName;
    private ApplicationStatus statusWithFunder;
    private LocalDateTime submittedAt;
    private BigDecimal amountAwarded;
}

