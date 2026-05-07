package com.commongrant.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DeadlineDto {
    private String title;
    private String source;
    private LocalDate dueDate;
    private int daysRemaining;
    private String urgency;   // "critical", "warning", "ok"
}

