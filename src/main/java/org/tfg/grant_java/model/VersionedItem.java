package org.tfg.grant_java.model;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Versioned field/question item")
public class VersionedItem {

    @Schema(example = "Mission Statement")
    private String fieldKey;

    @Schema(example = "PREDEFINED")
    private String type;

    @Schema(example = "2")
    private Integer currentVersion;

    @Schema(description = "All versions of the item")
    private Map<String, VersionEntry> versions;
}
