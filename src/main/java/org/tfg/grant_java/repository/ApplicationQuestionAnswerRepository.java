package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.grant_java.entity.ApplicationQuestionAnswerId;
import org.tfg.grant_java.entity.ApplicationQuestionAnswers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationQuestionAnswerRepository
        extends JpaRepository<ApplicationQuestionAnswers, ApplicationQuestionAnswerId> {
//    Optional<List<ApplicationQuestionAnswers>> findByApplication_ApplicationId(UUID uuid);
//
//    void deleteByApplication_ApplicationId(UUID applicationId);

    Optional<List<ApplicationQuestionAnswers>> findByApplicationId(UUID uuid);

    void deleteByApplicationId(UUID applicationId);
}
