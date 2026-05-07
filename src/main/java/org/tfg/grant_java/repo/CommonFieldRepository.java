package org.tfg.grant_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.domain.CommonField;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommonFieldRepository extends JpaRepository<CommonField, UUID> {
    List<CommonField> findByOrganisationIdOrderByDisplayOrderAsc(UUID organisationId);
    Optional<CommonField> findByOrganisationIdAndKey(UUID organisationId, String key);
    List<CommonField> findAllByOrderByDisplayOrderAsc();
    Optional<CommonField> findByKey(String key);
}
