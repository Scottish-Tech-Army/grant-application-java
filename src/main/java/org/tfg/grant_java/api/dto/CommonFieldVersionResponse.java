package org.tfg.grant_java.api.dto;

import java.time.Instant;

public class CommonFieldVersionResponse {
    private int v;
    private String valueText;
    private Instant createdAt;
    private String changeNote;

    public int getV() { return v; }
    public void setV(int v) { this.v = v; }
    public String getValueText() { return valueText; }
    public void setValueText(String valueText) { this.valueText = valueText; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
}

