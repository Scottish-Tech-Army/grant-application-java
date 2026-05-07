package org.tfg.grant_java.api.dto;

import jakarta.validation.constraints.NotBlank;

public class AddCommonFieldVersionRequest {
    @NotBlank
    private String valueText;
    private String changeNote;

    public String getValueText() { return valueText; }
    public void setValueText(String valueText) { this.valueText = valueText; }
    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
}

