package com.commongrant.dto;

import com.commongrant.model.GrantApplication.ApplicationStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationSummaryDto {
    private Long id;
    private String projectTitle;
    private BigDecimal amountRequested;
    private LocalDate submissionDeadline;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FunderSubmissionDto> funderSubmissions;
}

