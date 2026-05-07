package org.tfg.grant_java.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class GrantApplicationDetails {
    private UUID applicationId;
    private String applicantName;
    private String funderName;
    private String status;
    private Instant lastUpdatedDate;
    private String lastUpdatedBy;
}
