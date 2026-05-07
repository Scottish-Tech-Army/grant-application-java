package org.tfg.grant_java.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(
        name = "application_common_selection",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_app_field_once",
                columnNames = {"application_id", "common_field_id"}
        )
)
public class ApplicationCommonSelection {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private FundingApplication application;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "common_field_id", nullable = false)
    private CommonField commonField;

    @Column(nullable = false)
    private boolean included = true;

    /**
     * Version number of the CommonField value used for this application.
     * This is what satisfies "track which version was included".
     */
    @Column(nullable = false)
    private int versionUsed;

    /**
     * Optional: freeze the exact text used at the time the selection was saved/submitted.
     * This makes exports stable even if common field versions change later.
     */
    @Lob
    @Column(name = "snapshot_value")
    private String snapshotValue;

    public ApplicationCommonSelection() {
        // JPA only
    }

    public ApplicationCommonSelection(FundingApplication application,
                                      CommonField commonField,
                                      boolean included,
                                      int versionUsed,
                                      String snapshotValue) {
        this.application = application;
        this.commonField = commonField;
        this.included = included;
        this.versionUsed = versionUsed;
        this.snapshotValue = snapshotValue;
    }

    public UUID getId() {
        return id;
    }

    public FundingApplication getApplication() {
        return application;
    }

    public void setApplication(FundingApplication application) {
        this.application = application;
    }

    public CommonField getCommonField() {
        return commonField;
    }

    public void setCommonField(CommonField commonField) {
        this.commonField = commonField;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public int getVersionUsed() {
        return versionUsed;
    }

    public void setVersionUsed(int versionUsed) {
        this.versionUsed = versionUsed;
    }

    public String getSnapshotValue() {
        return snapshotValue;
    }

    public void setSnapshotValue(String snapshotValue) {
        this.snapshotValue = snapshotValue;
    }
}