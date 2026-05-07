package org.tfg.grant_java.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a grant application.
 *
 * Carries application details between controller and service layers.
 */
@Data
public class ApplicationDTO {
    private Long id;
    private String applicationNumber;
    private String projectName;
    private String funderName;
    private Long charityId;
    private Long commonDataId;
    private String status;
    private String selectedCommonKeys;
    private String commonDataJson;
    private String applicationDataJson;
    private String comments;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
