package org.tfg.grant_java.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GrantApplicationsRequestDTO {
    private String userId;
    private String tenantId;
    private int pageNumber;
    private int pageSize;
}
