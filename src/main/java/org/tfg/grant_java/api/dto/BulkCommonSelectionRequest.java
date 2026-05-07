package org.tfg.grant_java.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BulkCommonSelectionRequest {
    @NotNull
    @Valid
    private List<CommonSelectionRequest> selections;
    private boolean storeSnapshot;

    public List<CommonSelectionRequest> getSelections() { return selections; }
    public void setSelections(List<CommonSelectionRequest> selections) { this.selections = selections; }
    public boolean isStoreSnapshot() { return storeSnapshot; }
    public void setStoreSnapshot(boolean storeSnapshot) { this.storeSnapshot = storeSnapshot; }
}

