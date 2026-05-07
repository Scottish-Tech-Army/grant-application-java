package org.tfg.grant_java.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Application summary")
public class ApplicationListResponse {

    private String applicationId;
    private String name;
    private String funderName;
    private String status;
    private String createdDate;
}
