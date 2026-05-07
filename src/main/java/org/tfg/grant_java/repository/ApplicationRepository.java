package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.ApplicationEntity;

import java.util.List;
import java.util.Optional;


public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, String> {

    List<ApplicationEntity> findByUser_Id(String userId);

    Optional<ApplicationEntity> findByIdAndUser_Id(String applicationId, String userId);
}


