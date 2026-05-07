package com.commongrant.service;

import com.commongrant.config.ResourceNotFoundException;
import com.commongrant.dto.*;
import com.commongrant.model.*;
import com.commongrant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentVaultService {

    private final VaultDocumentRepository docRepo;
    private final OrganizationRepository orgRepo;

    private static final String UPLOAD_DIR = "uploads";

    public List<VaultDocumentDto> listByOrg(Long orgId) {
        return docRepo.findByOrganizationIdOrderByUploadedAtDesc(orgId)
                .stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public VaultDocumentDto upload(Long orgId, MultipartFile file,
                                   String documentType, String expiresAt) throws IOException {
        Organization org = orgRepo.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Org not found"));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        Files.createDirectories(uploadPath);
        String fileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.write(uploadPath.resolve(fileKey), file.getBytes());

        VaultDocument doc = VaultDocument.builder()
                .organization(org)
                .displayName(file.getOriginalFilename())
                .fileKey(fileKey)
                .contentType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .documentType(VaultDocument.DocumentType.valueOf(documentType))
                .expiresAt(expiresAt != null ? LocalDate.parse(expiresAt) : null)
                .build();

        return toDto(docRepo.save(doc));
    }

    @Transactional
    public void delete(Long id, Long orgId) {
        VaultDocument doc = docRepo.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        try { Files.deleteIfExists(Paths.get(UPLOAD_DIR, doc.getFileKey())); }
        catch (IOException ignored) {}
        docRepo.delete(doc);
    }

    private VaultDocumentDto toDto(VaultDocument d) {
        boolean expiring = d.getExpiresAt() != null &&
                ChronoUnit.DAYS.between(LocalDate.now(), d.getExpiresAt()) <= 30;
        return VaultDocumentDto.builder()
                .id(d.getId()).displayName(d.getDisplayName())
                .documentType(d.getDocumentType().name())
                .fileSizeBytes(d.getFileSizeBytes())
                .expiresAt(d.getExpiresAt())
                .uploadedAt(d.getUploadedAt())
                .reuseCount(d.getReuseCount())
                .expiringSoon(expiring)
                .build();
    }
}

