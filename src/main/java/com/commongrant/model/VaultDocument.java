package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vault_documents")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class VaultDocument {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    private String displayName;
    private String fileKey;          // S3 key or local path
    private String contentType;
    private Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private LocalDate expiresAt;

    @Builder.Default
    private int reuseCount = 0;

    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() { uploadedAt = LocalDateTime.now(); }

    public enum DocumentType {
        IRS_DETERMINATION_LETTER, FORM_990, AUDITED_FINANCIALS,
        BOARD_LIST, STRATEGIC_PLAN, BUDGET, SIGNATURE, OTHER
    }
}

