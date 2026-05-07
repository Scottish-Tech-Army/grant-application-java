package org.tfg.grant_java.api.dto;

import org.tfg.grant_java.domain.enums.CommonDataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommonFieldRequest {
    @NotBlank
    private String key;

    @NotBlank
    private String label;

    @Size(max = 2000)
    private String helpText;

    @NotNull
    private CommonDataType dataType;

    private int displayOrder;

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
}

