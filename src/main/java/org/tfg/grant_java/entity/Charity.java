package org.tfg.grant_java.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity representing a <b>Charity</b>.
 *
 * <p>This entity maps to the {@code CHARITY} table and stores
 * master information about a charity that can submit or own
 * grant applications within the system.</p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Holds core charity details such as name and active status</li>
 *   <li>Acts as a parent reference for users and applications</li>
 *   <li>Maintains creation audit metadata</li>
 * </ul>
 */
@Entity
@Table(name = "CHARITY")
@Getter
@Setter
@NoArgsConstructor
public class Charity {

    /**
     * Primary key of the charity record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Official name of the charity.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Indicates whether the charity is active in the system.
     *
     * <p>Defaults to {@code true}.</p>
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Identifier of the user who created this charity record.
     */
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * Timestamp when the charity record was created.
     *
     * <p>Automatically populated by Hibernate.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

