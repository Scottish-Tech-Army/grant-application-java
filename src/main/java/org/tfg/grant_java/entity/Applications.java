package org.tfg.grant_java.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "applications")
@Data
public class Applications {

    @Id
    @Column(name = "application_id", nullable = false, updatable = false)
    private UUID applicationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenants tenant;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "funder_name")
    private String funderName;

    @Column(name = "status", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

}
