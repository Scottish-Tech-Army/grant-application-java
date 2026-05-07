package com.commongrant.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SubmitRequest {
    @NotEmpty private List<Long> funderIds;
}

