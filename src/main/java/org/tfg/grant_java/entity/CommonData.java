package org.tfg.grant_java.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity representing reusable <b>Common Data</b> templates.
 *
 * <p>This entity maps to the {@code COMMON_DATA} table and stores
 * structured JSON-based data that can be shared across multiple
 * grant applications for a charity.</p>
 *
 * <p><b>Usage examples:</b></p>
 * <ul>
 *   <li>Standard application sections reused across submissions</li>
 *   <li>Versioned templates for grant forms</li>
 *   <li>Charity-specific common information</li>
 * </ul>
 */
@Entity
@Table(name = "COMMON_DATA")
@Getter
@Setter
@NoArgsConstructor
public class CommonData {

    /**
     * Primary key of the common data record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    /**
     * Option A: FK as plain column (recommended for simpler services)
     */
    @Column(name = "charity_id", nullable = false)
    private Long charityId;

    /**
     * Version identifier of the common data/template.
     *
     * <p>Useful for tracking changes over time.</p>
     */
    @Column(name = "version", length = 255)
    private String version;

    /**
     * CLOB / JSON data
     * Works for MySQL, Oracle, H2.
     * For Postgres JSONB, mapping can be adjusted.
     */
    @Lob
    @Column(name = "data_json", nullable = false, columnDefinition = "CLOB")
    private String dataJson;

    /**
     * Human-readable title for the template or common data set.
     */
    @Column(name = "template_title")
    private String templateTitle;

    /**
     * Description explaining the purpose or contents of this common data.
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Indicates whether this common data record is active.
     *
     * <p>Defaults to {@code true}.</p>
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
    /**
     * Identifier of the user who created this record.
     */
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * Timestamp when the record was created.
     *
     * <p>Automatically populated by Hibernate.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
