package org.tfg.grant_java.domain;

import org.tfg.grant_java.domain.enums.CommonDataType;
import org.tfg.grant_java.persistence.CommonFieldVersionSetConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(name = "common_field",
       uniqueConstraints = @UniqueConstraint(name = "uk_common_field_org_key",
               columnNames = {"organisation_id", "field_key"}))
public class CommonField extends AuditedEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @Column(name = "field_key", nullable = false)
    private String key;

    @Column(nullable = false)
    private String label;

    @Column(length = 2000)
    private String helpText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonDataType dataType = CommonDataType.TEXT;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private int displayOrder = 0;

    @Column(nullable = false)
    private int currentVersion = 0;

    @Lob
    @Column(name = "versions_json")
    @Convert(converter = CommonFieldVersionSetConverter.class)
    private CommonFieldVersionSet versionSet = new CommonFieldVersionSet();

    @Column(name = "created_by")
    private UUID createdBy;

    public CommonField() {}

    public UUID getId() { return id; }
    public Organisation getOrganisation() { return organisation; }
    public void setOrganisation(Organisation organisation) { this.organisation = organisation; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }
    public CommonDataType getDataType() { return dataType; }
    public void setDataType(CommonDataType dataType) { this.dataType = dataType; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public int getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(int currentVersion) { this.currentVersion = currentVersion; }
    public CommonFieldVersionSet getVersionSet() { return versionSet; }
    public void setVersionSet(CommonFieldVersionSet versionSet) { this.versionSet = versionSet; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
}

