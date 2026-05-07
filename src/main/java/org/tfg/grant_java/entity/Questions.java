package org.tfg.grant_java.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "questions")
public class Questions {

    @Id
    @Column(name = "question_id", updatable = false)
    private UUID questionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenants tenant;

    @Column(name = "question_text", nullable = false, columnDefinition = "text")
    private String questionText;

    // logical answer id (not an entity in your ERD)
    @Column(name = "default_answer_id")
    private UUID defaultAnswerId;

    // FK to ANSWER_VERSION.answer_version_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_answer_version_id")
    private AnswerVersions defaultAnswerVersion;

    // denormalized text snapshot
    @Column(name = "default_answer_text", columnDefinition = "text")
    private String defaultAnswerText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
