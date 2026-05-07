package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "funder_submissions")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FunderSubmission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private GrantApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funder_id", nullable = false)
    private Funder funder;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GrantApplication.ApplicationStatus statusWithFunder = GrantApplication.ApplicationStatus.SUBMITTED;

    private LocalDateTime submittedAt;
    private String funderNotes;
    private BigDecimal amountAwarded;
}

