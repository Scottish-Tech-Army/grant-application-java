package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_reports")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ComplianceReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private GrantApplication application;

    private String reportTitle;
    private String narrative;
    private Integer beneficiariesServed;
    private BigDecimal fundsExpended;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.DRAFT;

    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum ReportStatus { DRAFT, SUBMITTED }
}

