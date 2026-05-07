package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.AnswerVersions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerVersionRepository extends JpaRepository<AnswerVersions, UUID> {
    List<AnswerVersions> findByQuestion_QuestionIdOrderByVersionNumberDesc(UUID questionId);
    Optional<AnswerVersions> findTopByQuestion_QuestionIdOrderByVersionNumberDesc(UUID questionId);
}
