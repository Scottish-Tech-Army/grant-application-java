package org.tfg.grant_java.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Value object stored inside CommonFieldVersionSet JSON.
 * Not a JPA entity/table.
 */
public class CommonFieldVersion {

    private int v;                 // version number (1,2,3,...)
    private String valueText;      // stored value for that version
    private Instant createdAt;     // when this version was created
    private UUID createdBy;        // user who created this version
    private String changeNote;     // optional note about what changed

    public CommonFieldVersion() {
        // Needed for Jackson deserialization
    }

    public CommonFieldVersion(int v, String valueText, Instant createdAt, UUID createdBy, String changeNote) {
        this.v = v;
        this.valueText = valueText;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.changeNote = changeNote;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getChangeNote() {
        return changeNote;
    }

    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }
}