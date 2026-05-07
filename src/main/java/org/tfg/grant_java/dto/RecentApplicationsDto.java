package org.tfg.grant_java.dto;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.tfg.grant_java.entity.Tenants;

import java.time.Instant;
import java.util.UUID;

public class RecentApplicationsDto {

    private UUID applicationId;

    private String name;

    private String funderName;

    private String status;

    private Instant createdAt;

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunderName() {
        return funderName;
    }

    public void setFunderName(String funderName) {
        this.funderName = funderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
