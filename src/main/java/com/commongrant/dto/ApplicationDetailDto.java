package com.commongrant.dto;

import com.commongrant.model.GrantApplication.ApplicationStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationDetailDto {
    private Long id;
    private String projectTitle;
    private String projectDescription;
    private String targetPopulation;
    private String geographicArea;
    private BigDecimal amountRequested;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDate submissionDeadline;
    private String focusAreas;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FunderSubmissionDto> funderSubmissions;
}

