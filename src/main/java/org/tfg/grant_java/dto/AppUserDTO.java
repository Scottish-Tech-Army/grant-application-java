package org.tfg.grant_java.dto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing an application user.
 *
 * Carries user details between service and controller layers.
 */
@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private String role;
    private Long charityId;
    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdAt;
}