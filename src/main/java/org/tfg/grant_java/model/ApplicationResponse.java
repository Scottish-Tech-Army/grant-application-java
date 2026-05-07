package org.tfg.grant_java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response model representing a summary view of an application.
 *
 * <p>This DTO is used to expose high-level application information
 * in list or overview APIs, without including large JSON payloads
 * or detailed template data.</p>
 *
 * <p><b>Typical use cases:</b></p>
 * <ul>
 *   <li>Application listing screens</li>
 *   <li>Search or filter results</li>
 *   <li>Dashboard or summary views</li>
 * </ul>
 *
 * <p>This class is a read-only transport object and should not be
 * used for persistence operations.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationResponse {

    /**
     * Unique identifier of the application.
     */
    private Long id;

    /**
     * Human-readable application reference/number.
     */
    private String applicationNumber;

    /**
     * Name of the project for which funding is requested.
     */
    private String projectName;

    /**
     * Name of the funding organization.
     */
    private String funderName;

    /**
     * Identifier of the associated charity.
     */
    private Long charityId;

    /**
     * Identifier of the linked common data/template.
     */
    private Long commonDataId;

    /**
     * Current status of the application.
     */
    private String status;

    /**
     * Timestamp when the application was last modified.
     */
    private LocalDateTime modifiedAt;

}
