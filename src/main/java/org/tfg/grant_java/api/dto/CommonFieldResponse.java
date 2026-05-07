package org.tfg.grant_java.api.dto;

import org.tfg.grant_java.domain.enums.CommonDataType;
import java.time.Instant;
import java.util.UUID;

public class CommonFieldResponse {
    private UUID id;
    private String key;
    private String label;
    private String helpText;
    private CommonDataType dataType;
    private int displayOrder;
    private int currentVersion;
    private String currentValueText;
    private Instant lastUpdatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }
    public CommonDataType getDataType() { return dataType; }
    public void setDataType(CommonDataType dataType) { this.dataType = dataType; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public int getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(int currentVersion) { this.currentVersion = currentVersion; }
    public String getCurrentValueText() { return currentValueText; }
    public void setCurrentValueText(String currentValueText) { this.currentValueText = currentValueText; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}

