package org.tfg.grant_java.dto;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.tfg.grant_java.entity.Tenants;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UserDTO implements Serializable {

    private UUID userId;

    private String name;

    private String email;
    private Instant createdAt;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
