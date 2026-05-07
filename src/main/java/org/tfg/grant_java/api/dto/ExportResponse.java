package org.tfg.grant_java.api.dto;

import java.util.UUID;

public class ExportResponse {
    private UUID applicationId;
    private String plainText;

    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }
    public String getPlainText() { return plainText; }
    public void setPlainText(String plainText) { this.plainText = plainText; }
}

