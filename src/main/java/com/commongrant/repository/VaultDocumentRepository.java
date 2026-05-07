package com.commongrant.repository;

import com.commongrant.model.VaultDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaultDocumentRepository extends JpaRepository<VaultDocument, Long> {
    List<VaultDocument> findByOrganizationIdOrderByUploadedAtDesc(Long orgId);
    Optional<VaultDocument> findByIdAndOrganizationId(Long id, Long orgId);
}

