package org.tfg.grant_java.request;

import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {
    private String question;
    private String answer;
    private UUID userId;
    private UUID tenantId;
}
