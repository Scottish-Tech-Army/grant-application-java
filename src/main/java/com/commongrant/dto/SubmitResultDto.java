package com.commongrant.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SubmitResultDto {
    private Long applicationId;
    private List<String> submittedToFunders;
    private LocalDateTime submittedAt;
}

