package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.UserPredefinedFieldsEntity;

import java.util.Optional;


public interface UserPredefinedFieldsRepository
        extends JpaRepository<UserPredefinedFieldsEntity, String> {

    Optional<UserPredefinedFieldsEntity> findByUser_Id(String userId);
}
