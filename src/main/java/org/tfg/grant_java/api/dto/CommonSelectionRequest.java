package org.tfg.grant_java.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CommonSelectionRequest {
    @NotNull
    private UUID commonFieldId;
    private boolean included = true;
    @NotNull
    private Integer versionUsed;

    public UUID getCommonFieldId() { return commonFieldId; }
    public void setCommonFieldId(UUID commonFieldId) { this.commonFieldId = commonFieldId; }
    public boolean isIncluded() { return included; }
    public void setIncluded(boolean included) { this.included = included; }
    public Integer getVersionUsed() { return versionUsed; }
    public void setVersionUsed(Integer versionUsed) { this.versionUsed = versionUsed; }
}

