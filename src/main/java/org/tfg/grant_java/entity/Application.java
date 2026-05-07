package org.tfg.grant_java.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * JPA entity representing a grant/funding <b>Application</b>.
 *
 * <p>This entity maps to the {@code APPLICATION} table and captures:
 * application identifiers, project/funder details, references to related
 * charity and common data, and audit metadata (created/modified info).</p>
 *
 * <p><b>Notes</b>:</p>
 * <ul>
 *   <li>{@link #applicationDataJson} stores the raw application payload as JSON (CLOB).</li>
 *   <li>{@link #selectedCommonKeys} stores selected common-data keys (e.g. {@code [1,2]}) as a serialized value.</li>
 *   <li>{@link #createdAt} and {@link #modifiedAt} are managed automatically by Hibernate.</li>
 * </ul>
 */
@Entity
@Table(name = "APPLICATION")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Application {

    /**
     * Primary key of the application record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    /**
     * Identifier of the charity this application belongs to.
     *
     * <p>Stored as a foreign-key value (FK only) for simplicity.</p>
     */
    @Column(name = "charity_id", nullable = false)
    private Long charityId;

    /**
     * Human-readable application reference/number (if applicable).
     */
    @Column(name = "application_number", length = 255)
    private String applicationNumber;

    /**
     * Name of the project for which funding is requested.
     */
    @Column(name = "project_name", length = 255)
    private String projectName;

    /**
     * Name of the funding organization/body.
     */
    @Column(name = "funder_name", length = 255)
    private String funderName;

    /**
     * Reference identifier to the common data record (e.g., a row in {@code common_data}).
     */
    @Column(name = "common_data_id")
    private Long commonDataId;

    /**
     * Serialized list of selected common-data keys.
     *
     * <p>Example: {@code [1,2]}.</p>
     */
    @Column(name = "selected_common_keys", columnDefinition = "CLOB")
    private String selectedCommonKeys;

    /**
     * Raw application payload stored as JSON.
     *
     * <p>Persisted as a CLOB to support large content.</p>
     */
    @Lob
    @Column(name = "application_data_json", columnDefinition = "CLOB")
    private String applicationDataJson;

    /**
     * Free-text comments at the application level (e.g., status change notes, reviewer remarks).
     */
    @Lob
    @Column(name = "comments", length = 255)
    private String comments;

    /**
     * Current application status (e.g., DRAFT/SUBMITTED/APPROVED/REJECTED).
     *
     * <p>Kept as a string for flexibility; consider an enum mapping if statuses are fixed.</p>
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * Indicates whether this application record is active.
     *
     * <p>Defaults to {@code true}.</p>
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    /**
     * Timestamp when the record was created.
     *
     * <p>Automatically populated by Hibernate and not updatable.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Identifier of the user who last modified this application record.
     */
    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    /**
     * Timestamp when the record was last modified.
     *
     * <p>Automatically updated by Hibernate.</p>
     */
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

}