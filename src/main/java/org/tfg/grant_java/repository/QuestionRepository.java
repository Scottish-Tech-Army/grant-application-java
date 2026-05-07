package org.tfg.grant_java.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.Questions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Questions, UUID> {
    Page<Questions> findByTenant_TenantIdAndIsActiveTrue(
            UUID tenantId,
            Pageable pageable
    );

    Optional<Questions> findByQuestionIdAndTenant_TenantId(UUID questionId, UUID tenantId);

    List<Questions> findByQuestionIdIn(List<UUID> questionIds);

}
