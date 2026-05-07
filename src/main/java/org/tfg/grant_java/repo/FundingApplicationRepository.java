package org.tfg.grant_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.domain.FundingApplication;

import java.util.List;
import java.util.UUID;

public interface FundingApplicationRepository extends JpaRepository<FundingApplication, UUID> {

    List<FundingApplication> findAllByOrderByUpdatedAtDesc();
}
