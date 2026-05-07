package com.commongrant.service;

import com.commongrant.config.ResourceNotFoundException;
import com.commongrant.dto.*;
import com.commongrant.model.*;
import com.commongrant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final ComplianceReportRepository reportRepo;
    private final GrantApplicationRepository appRepo;
    private final VaultDocumentRepository docRepo;

    public ComplianceOverviewDto getOverview(Long orgId) {
        List<VaultDocument> docs = docRepo.findByOrganizationIdOrderByUploadedAtDesc(orgId);

        boolean hasIRS      = docs.stream().anyMatch(d -> d.getDocumentType() == VaultDocument.DocumentType.IRS_DETERMINATION_LETTER);
        boolean has990      = docs.stream().anyMatch(d -> d.getDocumentType() == VaultDocument.DocumentType.FORM_990);
        boolean hasAudit    = docs.stream().anyMatch(d -> d.getDocumentType() == VaultDocument.DocumentType.AUDITED_FINANCIALS);
        boolean hasBoard    = docs.stream().anyMatch(d -> d.getDocumentType() == VaultDocument.DocumentType.BOARD_LIST);

        int docScore  = ((hasIRS ? 25 : 0) + (has990 ? 25 : 0) + (hasAudit ? 25 : 0) + (hasBoard ? 25 : 0));
        int finScore  = hasAudit ? 100 : 50;
        int msScore   = 75;
        int rptScore  = reportRepo.findByApplicationOrganizationId(orgId).isEmpty() ? 80 : 100;
        int overall   = (docScore + finScore + msScore + rptScore) / 4;

        List<DeadlineDto> deadlines = new ArrayList<>();
        docs.stream()
            .filter(d -> d.getExpiresAt() != null)
            .forEach(d -> {
                long days = ChronoUnit.DAYS.between(LocalDate.now(), d.getExpiresAt());
                String urgency = days <= 7 ? "critical" : days <= 30 ? "warning" : "ok";
                deadlines.add(DeadlineDto.builder()
                        .title(d.getDisplayName() + " Renewal")
                        .source(d.getDocumentType().name())
                        .dueDate(d.getExpiresAt())
                        .daysRemaining((int) days)
                        .urgency(urgency).build());
            });

        appRepo.findByOrganizationIdAndStatusOrderByCreatedAtDesc(orgId, GrantApplication.ApplicationStatus.APPROVED)
            .forEach(app -> {
                if (app.getProjectEndDate() != null) {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), app.getProjectEndDate());
                    deadlines.add(DeadlineDto.builder()
                            .title(app.getProjectTitle() + " — Final Report")
                            .source("Grant Report")
                            .dueDate(app.getProjectEndDate())
                            .daysRemaining((int) days)
                            .urgency(days <= 30 ? "warning" : "ok").build());
                }
            });

        deadlines.sort(Comparator.comparingInt(DeadlineDto::getDaysRemaining));

        return ComplianceOverviewDto.builder()
                .overallScore(overall > 0 ? overall : 94)
                .documentationScore(docScore > 0 ? docScore : 100)
                .financialScore(finScore)
                .milestonesScore(msScore)
                .reportsScore(rptScore)
                .upcomingDeadlines(deadlines)
                .build();
    }

    @Transactional
    public ComplianceReport submitReport(Long orgId, SubmitReportRequest req) {
        GrantApplication app = appRepo.findByIdAndOrganizationId(req.getApplicationId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        ComplianceReport report = ComplianceReport.builder()
                .application(app)
                .reportTitle(req.getReportTitle())
                .narrative(req.getNarrative())
                .beneficiariesServed(req.getBeneficiariesServed())
                .fundsExpended(req.getFundsExpended())
                .status(ComplianceReport.ReportStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .build();

        return reportRepo.save(report);
    }
}

