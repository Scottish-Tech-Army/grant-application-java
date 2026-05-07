package com.commongrant.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FunderDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal minGrant;
    private BigDecimal maxGrant;
    private String focusAreas;
    private String geographicFocus;
    private LocalDate nextDeadline;
}

