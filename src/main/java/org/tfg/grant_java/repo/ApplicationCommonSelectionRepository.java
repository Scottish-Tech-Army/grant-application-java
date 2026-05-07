package org.tfg.grant_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.domain.ApplicationCommonSelection;

import java.util.List;
import java.util.UUID;

public interface ApplicationCommonSelectionRepository extends JpaRepository<ApplicationCommonSelection, UUID> {
    List<ApplicationCommonSelection> findByApplicationId(UUID applicationId);
    void deleteByApplicationId(UUID applicationId);
}

