package org.tfg.grant_java.dto;


import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class TenantDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID tenantId;

    private String name;

    private Instant createdAt;

    private String status;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
