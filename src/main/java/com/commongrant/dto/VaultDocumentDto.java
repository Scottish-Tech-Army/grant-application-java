package com.commongrant.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VaultDocumentDto {
    private Long id;
    private String displayName;
    private String documentType;
    private Long fileSizeBytes;
    private LocalDate expiresAt;
    private LocalDateTime uploadedAt;
    private int reuseCount;
    private boolean expiringSoon;
}

