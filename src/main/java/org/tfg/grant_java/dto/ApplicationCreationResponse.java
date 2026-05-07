package org.tfg.grant_java.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ApplicationCreationResponse {
    private UUID applicationId;
    private String status;
}
