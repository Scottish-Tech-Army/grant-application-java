package com.commongrant.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class SubmitReportRequest {
    @NotNull  private Long applicationId;
    @NotBlank private String reportTitle;
    private String narrative;
    private Integer beneficiariesServed;
    private BigDecimal fundsExpended;
}

