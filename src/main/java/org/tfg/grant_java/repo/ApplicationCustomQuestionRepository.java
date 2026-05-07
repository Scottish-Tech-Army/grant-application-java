package org.tfg.grant_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.domain.ApplicationCustomQuestion;

import java.util.List;
import java.util.UUID;

public interface ApplicationCustomQuestionRepository extends JpaRepository<ApplicationCustomQuestion, UUID> {
    List<ApplicationCustomQuestion> findByApplicationIdOrderByDisplayOrderAsc(UUID applicationId);
    void deleteByApplicationId(UUID applicationId);
}

