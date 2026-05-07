package org.tfg.grant_java.model;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Single version entry")
public class VersionEntry {

    @Schema(example = "We empower youth to focus on digital")
    private Object value;

    @Schema(example = "2026-03-03")
    private String timeStamp;
}
