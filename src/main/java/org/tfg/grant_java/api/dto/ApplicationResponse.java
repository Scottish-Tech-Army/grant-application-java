package org.tfg.grant_java.api.dto;

import org.tfg.grant_java.domain.enums.ApplicationStatus;
import org.tfg.grant_java.domain.enums.OutcomeStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class ApplicationResponse {
    private UUID id;
    private String name;
    private String funderName;
    private LocalDate deadline;
    private ApplicationStatus status;
    private Instant submittedAt;
    private OutcomeStatus outcome;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFunderName() { return funderName; }
    public void setFunderName(String funderName) { this.funderName = funderName; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public OutcomeStatus getOutcome() { return outcome; }
    public void setOutcome(OutcomeStatus outcome) { this.outcome = outcome; }
}

