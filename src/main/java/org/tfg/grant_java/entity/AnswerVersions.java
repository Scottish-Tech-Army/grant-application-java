package org.tfg.grant_java.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "answer_versions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_answer_version_question_answer_ver",
                        columnNames = {"question_id", "version_number"})
        })
public class AnswerVersions {

    @Id
    @Column(name = "answer_version_id", nullable = false, updatable = false)
    private UUID answerVersionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenants tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    // logical answer id across versions (not a separate entity per ERD)
    @Column(name = "answer_id")
    private UUID answerId;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "answer_text", nullable = false, columnDefinition = "text")
    private String answerText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;
}
