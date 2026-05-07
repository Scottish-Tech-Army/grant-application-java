package com.commongrant.repository;

import com.commongrant.model.FunderSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunderSubmissionRepository extends JpaRepository<FunderSubmission, Long> {
    List<FunderSubmission> findByApplicationId(Long applicationId);
}

