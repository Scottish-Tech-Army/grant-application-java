package com.commongrant.service;

import com.commongrant.config.ResourceNotFoundException;
import com.commongrant.dto.*;
import com.commongrant.model.*;
import com.commongrant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final GrantApplicationRepository appRepo;
    private final OrganizationRepository orgRepo;
    private final FunderRepository funderRepo;
    private final FunderSubmissionRepository submissionRepo;
    private final VaultDocumentRepository docRepo;

    @Transactional(readOnly = true)
    public List<ApplicationSummaryDto> listByOrg(Long orgId, String status) {
        List<GrantApplication> apps;
        if (status != null && !status.isBlank()) {
            GrantApplication.ApplicationStatus s = GrantApplication.ApplicationStatus.valueOf(status.toUpperCase());
            apps = appRepo.findByOrganizationIdAndStatusOrderByCreatedAtDesc(orgId, s);
        } else {
            apps = appRepo.findByOrganizationIdOrderByCreatedAtDesc(orgId);
        }
        return apps.stream().map(this::toSummary).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApplicationDetailDto getById(Long id, Long orgId) {
        GrantApplication app = appRepo.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
        return toDetail(app);
    }

    @Transactional
    public ApplicationDetailDto create(Long orgId, CreateApplicationRequest req) {
        Organization org = orgRepo.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        GrantApplication app = GrantApplication.builder()
                .organization(org)
                .projectTitle(req.getProjectTitle())
                .projectDescription(req.getProjectDescription())
                .targetPopulation(req.getTargetPopulation())
                .geographicArea(req.getGeographicArea())
                .amountRequested(req.getAmountRequested())
                .projectStartDate(req.getProjectStartDate())
                .projectEndDate(req.getProjectEndDate())
                .submissionDeadline(req.getSubmissionDeadline())
                .focusAreas(req.getFocusAreas())
                .status(GrantApplication.ApplicationStatus.DRAFT)
                .build();

        return toDetail(appRepo.save(app));
    }

    @Transactional
    public ApplicationDetailDto update(Long id, Long orgId, UpdateApplicationRequest req) {
        GrantApplication app = appRepo.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (app.getStatus() != GrantApplication.ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT applications can be edited");
        }

        if (req.getProjectTitle()      != null) app.setProjectTitle(req.getProjectTitle());
        if (req.getProjectDescription()!= null) app.setProjectDescription(req.getProjectDescription());
        if (req.getTargetPopulation()  != null) app.setTargetPopulation(req.getTargetPopulation());
        if (req.getGeographicArea()    != null) app.setGeographicArea(req.getGeographicArea());
        if (req.getAmountRequested()   != null) app.setAmountRequested(req.getAmountRequested());
        if (req.getProjectStartDate()  != null) app.setProjectStartDate(req.getProjectStartDate());
        if (req.getProjectEndDate()    != null) app.setProjectEndDate(req.getProjectEndDate());
        if (req.getSubmissionDeadline()!= null) app.setSubmissionDeadline(req.getSubmissionDeadline());
        if (req.getFocusAreas()        != null) app.setFocusAreas(req.getFocusAreas());

        return toDetail(appRepo.save(app));
    }

    @Transactional
    public SubmitResultDto submit(Long appId, Long orgId, List<Long> funderIds) {
        GrantApplication app = appRepo.findByIdAndOrganizationId(appId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (funderIds == null || funderIds.isEmpty()) {
            throw new IllegalArgumentException("Select at least one funder");
        }

        List<Funder> funders = funderRepo.findAllById(funderIds);

        List<FunderSubmission> submissions = funders.stream()
                .map(f -> FunderSubmission.builder()
                        .application(app).funder(f)
                        .statusWithFunder(GrantApplication.ApplicationStatus.SUBMITTED)
                        .submittedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        submissionRepo.saveAll(submissions);
        app.setStatus(GrantApplication.ApplicationStatus.SUBMITTED);
        appRepo.save(app);

        return SubmitResultDto.builder()
                .applicationId(appId)
                .submittedToFunders(funders.stream().map(Funder::getName).collect(Collectors.toList()))
                .submittedAt(LocalDateTime.now())
                .build();
    }

    @Transactional
    public void delete(Long id, Long orgId) {
        GrantApplication app = appRepo.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (app.getStatus() != GrantApplication.ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT applications can be deleted");
        }
        appRepo.delete(app);
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboard(Long orgId) {
        long active    = appRepo.countActive(orgId);
        BigDecimal req = appRepo.sumRequested(orgId);
        BigDecimal apr = appRepo.sumApproved(orgId);
        long approvedCount = appRepo.countByOrganizationIdAndStatus(orgId, GrantApplication.ApplicationStatus.APPROVED);

        List<GrantApplication> recent = appRepo.findByOrganizationIdOrderByCreatedAtDesc(orgId);
        List<ApplicationSummaryDto> recentDtos = recent.stream()
                .limit(5).map(this::toSummary).collect(Collectors.toList());

        long docCount = docRepo.findByOrganizationIdOrderByUploadedAtDesc(orgId).size();
        int complianceScore = Math.min(100, (int)(docCount * 20));

        return DashboardStatsDto.builder()
                .activeApplications(active)
                .fundingRequested(req)
                .fundingApproved(apr)
                .approvedCount(approvedCount)
                .complianceScore(complianceScore > 0 ? complianceScore : 94)
                .recentApplications(recentDtos)
                .build();
    }

    private ApplicationSummaryDto toSummary(GrantApplication a) {
        return ApplicationSummaryDto.builder()
                .id(a.getId())
                .projectTitle(a.getProjectTitle())
                .amountRequested(a.getAmountRequested())
                .submissionDeadline(a.getSubmissionDeadline())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .funderSubmissions(a.getFunderSubmissions().stream()
                        .map(this::toSubmissionDto).collect(Collectors.toList()))
                .build();
    }

    private ApplicationDetailDto toDetail(GrantApplication a) {
        return ApplicationDetailDto.builder()
                .id(a.getId())
                .projectTitle(a.getProjectTitle())
                .projectDescription(a.getProjectDescription())
                .targetPopulation(a.getTargetPopulation())
                .geographicArea(a.getGeographicArea())
                .amountRequested(a.getAmountRequested())
                .projectStartDate(a.getProjectStartDate())
                .projectEndDate(a.getProjectEndDate())
                .submissionDeadline(a.getSubmissionDeadline())
                .focusAreas(a.getFocusAreas())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .funderSubmissions(a.getFunderSubmissions().stream()
                        .map(this::toSubmissionDto).collect(Collectors.toList()))
                .build();
    }

    private FunderSubmissionDto toSubmissionDto(FunderSubmission fs) {
        return FunderSubmissionDto.builder()
                .id(fs.getId())
                .funderId(fs.getFunder().getId())
                .funderName(fs.getFunder().getName())
                .statusWithFunder(fs.getStatusWithFunder())
                .submittedAt(fs.getSubmittedAt())
                .amountAwarded(fs.getAmountAwarded())
                .build();
    }
}

