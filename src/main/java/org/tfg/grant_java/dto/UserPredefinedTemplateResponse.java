package org.tfg.grant_java.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.tfg.grant_java.model.VersionedItem;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User predefined fields and questions")
public class UserPredefinedTemplateResponse {

    @Schema(description = "User identifier")
    private String userId;

    @Schema(description = "List of predefined and custom fields")
    private List<VersionedItem> fields;

    @Schema(description = "List of predefined and custom questions")
    private List<VersionedItem> questions;
}
