package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grant_applications")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GrantApplication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String projectTitle;

    @Column(columnDefinition = "TEXT")
    private String projectDescription;

    private String targetPopulation;
    private String geographicArea;
    private BigDecimal amountRequested;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDate submissionDeadline;

    private String focusAreas;    // comma-separated

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FunderSubmission> funderSubmissions = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum ApplicationStatus {
        DRAFT, SUBMITTED, IN_REVIEW, APPROVED, DECLINED
    }
}

