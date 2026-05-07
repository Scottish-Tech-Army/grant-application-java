package org.tfg.grant_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.domain.Organisation;

import java.util.UUID;

public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {
}

