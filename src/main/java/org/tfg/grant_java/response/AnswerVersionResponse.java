package org.tfg.grant_java.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVersionResponse {
    private Integer version;
    private String answer;
    private String updatedBy;
    private Instant updatedDate;
}
