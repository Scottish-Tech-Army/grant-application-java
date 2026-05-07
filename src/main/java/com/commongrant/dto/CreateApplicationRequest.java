package com.commongrant.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateApplicationRequest {
    @NotBlank private String projectTitle;
    private String projectDescription;
    private String targetPopulation;
    private String geographicArea;
    @NotNull private BigDecimal amountRequested;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDate submissionDeadline;
    private String focusAreas;
}

