package org.tfg.grant_java.service;

import org.springframework.stereotype.Service;
import org.tfg.grant_java.api.dto.ExportResponse;
import org.tfg.grant_java.domain.*;
import org.tfg.grant_java.exception.BadRequestException;
import org.tfg.grant_java.exception.NotFoundException;
import org.tfg.grant_java.repo.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportService {
    private final FundingApplicationRepository applicationRepository;
    private final ApplicationCommonSelectionRepository selectionRepository;
    private final ApplicationCustomQuestionRepository questionRepository;

    public ExportService(FundingApplicationRepository applicationRepository,
                        ApplicationCommonSelectionRepository selectionRepository,
                        ApplicationCustomQuestionRepository questionRepository) {
        this.applicationRepository = applicationRepository;
        this.selectionRepository = selectionRepository;
        this.questionRepository = questionRepository;
    }

    public ExportResponse exportApplication(UUID orgId, UUID appId) {
        FundingApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
       /* if (!app.getOrganisation().getId().equals(orgId)) {
            throw new BadRequestException("Application does not belong to organisation");
        }*/
        StringBuilder sb = new StringBuilder();
        sb.append("APPLICATION META\n");
        sb.append("Name: ").append(app.getName()).append("\n");
        sb.append("Funder: ").append(app.getFunderName() != null ? app.getFunderName() : "").append("\n");
        sb.append("Deadline: ").append(app.getDeadline() != null ? app.getDeadline().format(DateTimeFormatter.ISO_DATE) : "").append("\n");
        sb.append("Status: ").append(app.getStatus()).append("\n\n");

        sb.append("COMMON INFORMATION\n");
        List<ApplicationCommonSelection> selections = selectionRepository.findByApplicationId(appId)
                .stream().filter(ApplicationCommonSelection::isIncluded).collect(Collectors.toList());
        selections.sort(Comparator.comparingInt(s -> s.getCommonField().getDisplayOrder()));
        for (ApplicationCommonSelection sel : selections) {
            CommonField field = sel.getCommonField();
            sb.append(field.getLabel()).append(": ");
            String value = (sel.getSnapshotValue() != null && !sel.getSnapshotValue().isBlank())
                    ? sel.getSnapshotValue()
                    : resolveValueForVersion(field, sel.getVersionUsed());
            sb.append(value != null ? value : "").append("\n");
        }
        sb.append("\nAPPLICATION SPECIFIC QUESTIONS\n");
        List<ApplicationCustomQuestion> questions = questionRepository.findByApplicationIdOrderByDisplayOrderAsc(appId);
        for (ApplicationCustomQuestion q : questions) {
            sb.append(q.getQuestionText()).append("\n");
            sb.append(q.getAnswerText() != null ? q.getAnswerText() : "").append("\n\n");
        }
        ExportResponse resp = new ExportResponse();
        resp.setApplicationId(appId);
        resp.setPlainText(sb.toString());
        return resp;
    }

    private String resolveValueForVersion(CommonField field, int versionUsed) {
        if (field.getVersionSet() == null || field.getVersionSet().getVersions() == null) return "[VERSION NOT FOUND]";
        return field.getVersionSet().getVersions().stream()
                .filter(v -> v.getV() == versionUsed)
                .findFirst()
                .map(CommonFieldVersion::getValueText)
                .orElse("[VERSION NOT FOUND]");
    }
}

