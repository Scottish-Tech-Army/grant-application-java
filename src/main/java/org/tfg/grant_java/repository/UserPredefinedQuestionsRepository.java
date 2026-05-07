package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.UserPredefinedQuestionsEntity;

import java.util.Optional;


public interface UserPredefinedQuestionsRepository
        extends JpaRepository<UserPredefinedQuestionsEntity, String> {

    Optional<UserPredefinedQuestionsEntity> findByUser_Id(String userId);
}


