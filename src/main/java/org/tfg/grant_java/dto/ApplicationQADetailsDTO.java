package org.tfg.grant_java.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ApplicationQADetailsDTO {

    private UUID questionId;
    private String section;
    private String question;
    private String answer;
    private boolean checked;
}
