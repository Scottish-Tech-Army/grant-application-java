package org.tfg.grant_java.dto;

import java.io.Serializable;
import java.util.UUID;

public class DashboardRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;

    private UUID tenantId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }


}

