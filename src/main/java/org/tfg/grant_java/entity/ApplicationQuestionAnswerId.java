package org.tfg.grant_java.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ApplicationQuestionAnswerId implements Serializable {

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "question_id")
    private UUID questionId;
}
