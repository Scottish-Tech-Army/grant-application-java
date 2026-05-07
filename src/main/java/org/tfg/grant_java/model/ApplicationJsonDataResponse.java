package org.tfg.grant_java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response model representing combined application and common data details.
 *
 * <p>This DTO is typically used in API responses to return a flattened view
 * of an application along with its associated common/template data.</p>
 *
 * <p><b>Purpose:</b></p>
 * <ul>
 *   <li>Expose application metadata and status</li>
 *   <li>Include selected common data and template information</li>
 *   <li>Avoid exposing internal entity relationships directly</li>
 * </ul>
 *
 * <p>This class is a read-only transport object and should not be used
 * for persistence.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplicationJsonDataResponse {

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
     * Serialized list of selected common-data keys.
     *
     * <p>Example: {@code [1,2]}.</p>
     */
    private String selectedCommonKeys;

    /**
     * Raw JSON payload of the application data.
     */
    private String applicationDataJson;

    /**
     * Version of the associated common data/template.
     */
    private String version;

    /**
     * Title of the common data template.
     */
    private String templateTitle;
    private String dataJson;
    private String comments;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
