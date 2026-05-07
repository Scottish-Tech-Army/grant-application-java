package org.tfg.grant_java.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.api.dto.*;
import org.tfg.grant_java.domain.*;
import org.tfg.grant_java.domain.enums.ApplicationStatus;
import org.tfg.grant_java.exception.BadRequestException;
import org.tfg.grant_java.exception.NotFoundException;
import org.tfg.grant_java.repo.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final FundingApplicationRepository applicationRepository;
    private final CommonFieldRepository commonFieldRepository;
    private final ApplicationCommonSelectionRepository selectionRepository;

    public ApplicationService(FundingApplicationRepository applicationRepository,
                             CommonFieldRepository commonFieldRepository,
                             ApplicationCommonSelectionRepository selectionRepository) {
        this.applicationRepository = applicationRepository;
        this.commonFieldRepository = commonFieldRepository;
        this.selectionRepository = selectionRepository;
    }

    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest req, UUID actorUserId) {
        FundingApplication app = new FundingApplication();
        app.setName(req.getName());
        app.setFunderName(req.getFunderName());
        app.setDeadline(req.getDeadline());
        app.setStatus(ApplicationStatus.DRAFT);
        app.setCreatedBy(actorUserId);
        FundingApplication saved = applicationRepository.save(app);
        return toResponse(saved);
    }

    public List<ApplicationResponse> listApplications() {
        List<FundingApplication> apps = applicationRepository.findAllByOrderByUpdatedAtDesc();
        return apps.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void updateCommonSelections(UUID appId, BulkCommonSelectionRequest req) {
        FundingApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
        selectionRepository.deleteByApplicationId(appId);
        List<ApplicationCommonSelection> selections = new ArrayList<>();
        for (CommonSelectionRequest selReq : req.getSelections()) {
            CommonField field = commonFieldRepository.findById(selReq.getCommonFieldId())
                    .orElseThrow(() -> new NotFoundException("CommonField not found"));
            ApplicationCommonSelection sel = new ApplicationCommonSelection();
            sel.setApplication(app);
            sel.setCommonField(field);
            sel.setIncluded(selReq.isIncluded());
            sel.setVersionUsed(selReq.getVersionUsed());
            if (req.isStoreSnapshot()) {
                String value = resolveValueForVersion(field, selReq.getVersionUsed());
                sel.setSnapshotValue(value);
            }
            selections.add(sel);
        }
        selectionRepository.saveAll(selections);
    }

    public String resolveValueForVersion(CommonField field, int versionUsed) {
        if (field.getVersionSet() == null || field.getVersionSet().getVersions() == null) return "[VERSION NOT FOUND]";
        return field.getVersionSet().getVersions().stream()
                .filter(v -> v.getV() == versionUsed)
                .findFirst()
                .map(CommonFieldVersion::getValueText)
                .orElse("[VERSION NOT FOUND]");
    }

    private ApplicationResponse toResponse(FundingApplication app) {
        ApplicationResponse dto = new ApplicationResponse();
        dto.setId(app.getId());
        dto.setName(app.getName());
        dto.setFunderName(app.getFunderName());
        dto.setDeadline(app.getDeadline());
        dto.setStatus(app.getStatus());
        dto.setSubmittedAt(app.getSubmittedAt());
        dto.setOutcome(app.getOutcome());
        return dto;
    }
}
