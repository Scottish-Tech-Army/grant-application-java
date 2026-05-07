package org.tfg.grant_java.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ApplicationDetailsDTO {

    private UUID applicationId;
    private String userId;
    private UUID tenantId;
    private String applicationName;
    private String funderName;
    private String status;
    private List<ApplicationQADetailsDTO> applicationQADetails;

}
