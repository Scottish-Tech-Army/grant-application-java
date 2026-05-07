package org.tfg.grant_java.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSummaryResponse {
    private UUID id;
    private String question;
    private String answer;
    private Integer currentVersion;
    private boolean isActive;
    private Instant createdAt;
}
