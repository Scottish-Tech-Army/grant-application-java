package org.tfg.grant_java.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.api.dto.*;
import org.tfg.grant_java.domain.*;
import org.tfg.grant_java.domain.enums.CommonDataType;
import org.tfg.grant_java.exception.BadRequestException;
import org.tfg.grant_java.exception.NotFoundException;
import org.tfg.grant_java.repo.CommonFieldRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonFieldService {
    private final CommonFieldRepository commonFieldRepository;

    public CommonFieldService(CommonFieldRepository commonFieldRepository) {
        this.commonFieldRepository = commonFieldRepository;
    }

    public List<CommonFieldResponse> listCommonFields() {
        return commonFieldRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public CommonFieldResponse createCommonField(CreateCommonFieldRequest req, UUID actorUserId) {
        if (commonFieldRepository.findByKey(req.getKey()).isPresent()) {
            throw new BadRequestException("Field key already exists");
        }
        CommonField field = new CommonField();
        field.setKey(req.getKey());
        field.setLabel(req.getLabel());
        field.setHelpText(req.getHelpText());
        field.setDataType(req.getDataType() != null ? req.getDataType() : CommonDataType.TEXT);
        field.setDisplayOrder(req.getDisplayOrder());
        field.setCreatedBy(actorUserId);
        // Initialize version set
        CommonFieldVersionSet versionSet = new CommonFieldVersionSet();
        versionSet.setCurrentVersion(1);
        CommonFieldVersion version = new CommonFieldVersion(1, "", Instant.now(), actorUserId, "Seeded");
        versionSet.getVersions().add(version);
        field.setCurrentVersion(1);
        field.setVersionSet(versionSet);
        CommonField saved = commonFieldRepository.save(field);
        return toResponse(saved);
    }

    @Transactional
    public CommonFieldResponse addNewVersion(UUID fieldId, AddCommonFieldVersionRequest req, UUID actorUserId) {
        CommonField field = commonFieldRepository.findById(fieldId)
                .orElseThrow(() -> new NotFoundException("CommonField not found"));
        CommonFieldVersionSet versionSet = field.getVersionSet();
        int nextV = Math.max(field.getCurrentVersion(), versionSet.getCurrentVersion()) + 1;
        CommonFieldVersion version = new CommonFieldVersion(nextV, req.getValueText(), Instant.now(), actorUserId, req.getChangeNote());
        versionSet.getVersions().add(version);
        versionSet.setCurrentVersion(nextV);
        field.setCurrentVersion(nextV);
        field.setVersionSet(versionSet);
        CommonField saved = commonFieldRepository.save(field);
        return toResponse(saved);
    }

    public List<CommonFieldVersionResponse> getVersions(UUID fieldId) {
        CommonField field = commonFieldRepository.findById(fieldId)
                .orElseThrow(() -> new NotFoundException("CommonField not found"));
        return field.getVersionSet().getVersions().stream()
                .map(this::toVersionResponse)
                .collect(Collectors.toList());
    }

    private CommonFieldResponse toResponse(CommonField field) {
        CommonFieldResponse dto = new CommonFieldResponse();
        dto.setId(field.getId());
        dto.setKey(field.getKey());
        dto.setLabel(field.getLabel());
        dto.setHelpText(field.getHelpText());
        dto.setDataType(field.getDataType());
        dto.setDisplayOrder(field.getDisplayOrder());
        dto.setCurrentVersion(field.getCurrentVersion());
        CommonFieldVersionSet versionSet = field.getVersionSet();
        if (versionSet != null && !versionSet.getVersions().isEmpty()) {
            CommonFieldVersion latest = versionSet.getVersions().stream()
                    .max(Comparator.comparingInt(CommonFieldVersion::getV)).orElse(null);
            if (latest != null) {
                dto.setCurrentValueText(latest.getValueText());
                dto.setLastUpdatedAt(latest.getCreatedAt());
            }
        }
        return dto;
    }

    private CommonFieldVersionResponse toVersionResponse(CommonFieldVersion v) {
        CommonFieldVersionResponse dto = new CommonFieldVersionResponse();
        dto.setV(v.getV());
        dto.setValueText(v.getValueText());
        dto.setCreatedAt(v.getCreatedAt());
        dto.setChangeNote(v.getChangeNote());
        return dto;
    }
}
