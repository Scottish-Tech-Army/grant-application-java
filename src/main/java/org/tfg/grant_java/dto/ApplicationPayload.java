package org.tfg.grant_java.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tfg.grant_java.model.VersionedItem;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationPayload {

    private String applicationId;
    private String userId;
    private String name;
    private String funderName;
    private String status;
    private String startDate;
    private String endDate;
    private String createDate;

    private List<VersionedItem> fields;
    private List<VersionedItem> questions;
}

