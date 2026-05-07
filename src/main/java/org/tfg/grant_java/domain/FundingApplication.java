package org.tfg.grant_java.domain;

import org.tfg.grant_java.domain.enums.ApplicationStatus;
import org.tfg.grant_java.domain.enums.OutcomeStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "funding_application")
public class FundingApplication extends AuditedEntity {
    @Id
    @UuidGenerator
    private UUID id;

    /*@ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;*/

    @Column(nullable = false)
    private String name;

    private String funderName;
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "created_by")
    private UUID createdBy;

    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    private OutcomeStatus outcome = OutcomeStatus.UNKNOWN;

    private Instant archivedAt;

    public FundingApplication() {}

    public UUID getId() { return id; }
   /* public Organisation getOrganisation() { return organisation; }
    public void setOrganisation(Organisation organisation) { this.organisation = organisation; }*/
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFunderName() { return funderName; }
    public void setFunderName(String funderName) { this.funderName = funderName; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public OutcomeStatus getOutcome() { return outcome; }
    public void setOutcome(OutcomeStatus outcome) { this.outcome = outcome; }
    public Instant getArchivedAt() { return archivedAt; }
    public void setArchivedAt(Instant archivedAt) { this.archivedAt = archivedAt; }
}

