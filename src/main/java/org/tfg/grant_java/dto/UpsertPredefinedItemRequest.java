package org.tfg.grant_java.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertPredefinedItemRequest {

    @Schema(example = "Mission Statement")
    @NotBlank
    private String fieldKey;

    @Schema(example = "We empower youth to focus on digital inclusion")
    @NotNull
    private Object value;
}
