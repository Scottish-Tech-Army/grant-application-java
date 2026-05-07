package org.tfg.grant_java.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "application_question_answers")
public class ApplicationQuestionAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @MapsId("questionId")
//    @JoinColumn(name = "question_id", nullable = true)
//    private Questions question;
    @Column(name = "question_id")
    private UUID questionId;
    // logical answer id (not an entity per ERD)
    @Column(name = "answer_text", nullable = false)
    private String answerText;

    // pinned_version FK -> ANSWER_VERSION.answer_version_id
    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "section", nullable = false)
    private String section;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;
}
