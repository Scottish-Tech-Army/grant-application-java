package org.tfg.grant_java.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a charity.
 *
 * Carries charity details between service and controller layers.
 */
@Data
public class CharityDTO {

    private Long id;

    private String name;

    private Boolean isActive;

    private String createdBy;
    private LocalDateTime createdAt;

}
