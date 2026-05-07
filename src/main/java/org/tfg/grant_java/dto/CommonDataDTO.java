package org.tfg.grant_java.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing common data templates.
 *
 * Used to carry common data details between service and controller layers.
 */
@Data
public class CommonDataDTO {

    private Long id;
    private Long charityId;
    private String version;
    private String dataJson;
    private String templateTitle;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
}
