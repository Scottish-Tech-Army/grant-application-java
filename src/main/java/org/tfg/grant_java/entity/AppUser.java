package org.tfg.grant_java.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity representing an application user of the system.
 *
 * <p>This entity maps to the {@code APP_USERS} table and stores
 * authentication, authorization, and audit-related information
 * for users accessing the grant application platform.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Holds login credentials and role-based access information</li>
 *   <li>Associates a user with a specific charity (via {@code charityId})</li>
 *   <li>Tracks user lifecycle and creation metadata</li>
 * </ul>
 */
@Entity
@Table(name = "APP_USERS")
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    /**
     * Primary key of the user record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    /**
     * Unique username used for authentication.
     */
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    /**
     * Encrypted password for the user.
     *
     * <p>Plain-text passwords must never be stored.</p>
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * Role assigned to the user, controlling access and permissions.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role; // ADMIN, USER

    /**
     * Option A (recommended): relationship mapping
     * If you already have Charity entity mapped to table charity.
     */
  /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id")
    private Charity charity;*/

    @JoinColumn(name = "charity_id")
    private Long charityId;

    /**
     * Indicates whether the user account is active.
     *
     * <p>Defaults to {@code true}.</p>
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * Timestamp when the user record was created.
     *
     * <p>Automatically populated by Hibernate.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Enumeration of supported user roles.
     */
    public enum Role {
        ADMIN, USER
    }
}