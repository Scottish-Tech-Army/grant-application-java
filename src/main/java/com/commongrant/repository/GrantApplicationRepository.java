package com.commongrant.repository;

import com.commongrant.model.GrantApplication;
import com.commongrant.model.GrantApplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrantApplicationRepository extends JpaRepository<GrantApplication, Long> {

    List<GrantApplication> findByOrganizationIdOrderByCreatedAtDesc(Long orgId);

    List<GrantApplication> findByOrganizationIdAndStatusOrderByCreatedAtDesc(Long orgId, ApplicationStatus status);

    Optional<GrantApplication> findByIdAndOrganizationId(Long id, Long orgId);

    long countByOrganizationIdAndStatus(Long orgId, ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM GrantApplication a WHERE a.organization.id = :orgId " +
           "AND a.status IN ('SUBMITTED', 'IN_REVIEW')")
    long countActive(@Param("orgId") Long orgId);

    @Query("SELECT COALESCE(SUM(a.amountRequested), 0) FROM GrantApplication a WHERE a.organization.id = :orgId")
    BigDecimal sumRequested(@Param("orgId") Long orgId);

    @Query("SELECT COALESCE(SUM(fs.amountAwarded), 0) FROM FunderSubmission fs " +
           "WHERE fs.application.organization.id = :orgId AND fs.amountAwarded IS NOT NULL")
    BigDecimal sumApproved(@Param("orgId") Long orgId);
}

