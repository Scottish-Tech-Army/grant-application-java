package org.tfg.grant_java.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.ApplicationListResponse;
import org.tfg.grant_java.dto.ApplicationPayload;
import org.tfg.grant_java.entity.ApplicationEntity;
import org.tfg.grant_java.entity.UserEntity;
import org.tfg.grant_java.entity.UserPredefinedFieldsEntity;
import org.tfg.grant_java.entity.UserPredefinedQuestionsEntity;
import org.tfg.grant_java.model.VersionEntry;
import org.tfg.grant_java.model.VersionedItem;
import org.tfg.grant_java.repository.ApplicationRepository;
import org.tfg.grant_java.repository.UserPredefinedFieldsRepository;
import org.tfg.grant_java.repository.UserPredefinedQuestionsRepository;
import org.tfg.grant_java.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationService {


    private final ApplicationRepository repo;


    private final UserRepository userRepository;


    private final UserPredefinedFieldsRepository userPredefinedFieldsRepository;


    private final UserPredefinedQuestionsRepository userPredefinedQuestionsRepository;

    @Autowired
    public ApplicationService(ApplicationRepository repo, UserRepository userRepository, UserPredefinedFieldsRepository userPredefinedFieldsRepository, UserPredefinedQuestionsRepository userPredefinedQuestionsRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.userPredefinedFieldsRepository = userPredefinedFieldsRepository;
        this.userPredefinedQuestionsRepository = userPredefinedQuestionsRepository;
    }

    public ApplicationEntity create(ApplicationPayload payload) {

        UserEntity user = userRepository.findById(payload.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + payload.getUserId()));

        ApplicationEntity entity = ApplicationEntity.builder()
                .user(user)
                .name(payload.getName())
                .funderName(payload.getFunderName())
                .status(payload.getStatus())
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .createDate(payload.getCreateDate())
                .fields(payload.getFields())
                .questions(payload.getQuestions())
                .build();

        ApplicationEntity saved = repo.save(entity);

        try {
            reconcilePredefinedFieldsOnSubmit(saved.getId());
            reconcilePredefinedQuestionsOnSubmit(saved.getId());
        } catch (Exception e) {
            log.info("Exception while updating the fields and questionary:  {}", e.getMessage());
        }

        return saved;
    }

    @Transactional
    public void reconcilePredefinedFieldsOnSubmit(String appId) throws Exception {

        log.info("Reconcile predefined fields started | appId={}", appId);


        ApplicationEntity app = repo.findById(appId)
                .orElseThrow(() -> {
                    log.warn("Application not found | appId={}", appId);
                    return new RuntimeException("Application not found: " + appId);
                });


        String userId = app.getUser().getId();
        String today = LocalDate.now().toString();


        UserPredefinedFieldsEntity userTemplate = userPredefinedFieldsRepository.findByUser_Id(userId)
                .orElseThrow(() -> {
                    log.warn("User predefined fields not found | userId={} appId={}", userId, appId);
                    return new RuntimeException("User predefined fields missing: " + userId);
                });


        // For each PREDEFINED field in the application
        for (VersionedItem appField : app.getFields()) {


            if (!"PREDEFINED".equalsIgnoreCase(appField.getType())) {
                log.debug("Skip non-predefined field | fieldKey={} type={}", appField.getFieldKey(), appField.getType());
                continue;
            }


            // Find SAME fieldKey in user predefined table
            VersionedItem templateField = userTemplate.getFields().stream()
                    .filter(f -> f.getFieldKey().equalsIgnoreCase(appField.getFieldKey()))
                    .findFirst()
                    .orElse(null);

            // if not found, do NOTHING (do not add)
            if (templateField == null) continue;

            // Take the application current version value
            VersionEntry appCurrent = currentEntry(appField);
            if (appCurrent == null) continue;

            Object newValue = appCurrent.getValue();

            // If value already exists in template versions -> do nothing

            if (valueExistsInTemplate(templateField, newValue)) {
                log.info("No change (value already exists) | fieldKey={} userId={} appId={} appCurrentVersion={}",
                        appField.getFieldKey(), userId, appId, appField.getCurrentVersion());
                continue;
            }


            userTemplate.setFields(new ArrayList<>(userTemplate.getFields()));
            // add as new version to user predefined field
            appendNewTemplateVersion(templateField, newValue, today);
        }
        userPredefinedFieldsRepository.save(userTemplate);

        log.info("Reconcile predefined fields completed | appId={} userId={}",
                appId, userId);
    }


    @Transactional
    public void reconcilePredefinedQuestionsOnSubmit(String appId) throws Exception {

        ApplicationEntity app = repo.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + appId));

        String userId = app.getUser().getId();
        String today = LocalDate.now().toString();

        UserPredefinedQuestionsEntity userTemplate = userPredefinedQuestionsRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("User predefined questions missing for user: " + userId));

        //  loop application questions
        for (VersionedItem appQuestion : app.getQuestions()) {

            // only PREDEFINED questions
            if (!"PREDEFINED".equalsIgnoreCase(appQuestion.getType())) continue;

            // find same question in user's predefined question list
            VersionedItem templateQuestion = userTemplate.getQuestions().stream()
                    .filter(q -> q.getFieldKey().equalsIgnoreCase(appQuestion.getFieldKey()))
                    .findFirst()
                    .orElse(null);

            //do NOT add if missing
            if (templateQuestion == null) continue;

            // fetch application current answer value
            VersionEntry appCurrent = currentEntry(appQuestion);
            if (appCurrent == null) continue;

            Object newValue = appCurrent.getValue();

            //if same value already exists in template → do nothing
            if (valueExistsInTemplate(templateQuestion, newValue)) continue;

            // else add as a new version in user predefined template
            appendNewTemplateVersion(templateQuestion, newValue, today);
        }

        // IMPORTANT for converter-based JSON columns: force dirty tracking
        userTemplate.setQuestions(new ArrayList<>(userTemplate.getQuestions()));

        userPredefinedQuestionsRepository.save(userTemplate);
    }


    private VersionEntry currentEntry(VersionedItem item) {
        if (item == null || item.getVersions() == null || item.getCurrentVersion() == null) return null;
        return item.getVersions().get(String.valueOf(item.getCurrentVersion()));
    }


    private boolean valueExistsInTemplate(VersionedItem templateField, Object value) {
        if (templateField.getVersions() == null) return false;

        return templateField.getVersions().values().stream()
                .anyMatch(v -> Objects.equals(v.getValue(), value));
    }

    private void appendNewTemplateVersion(VersionedItem templateField, Object newValue, String today) {
        if (templateField.getVersions() == null) templateField.setVersions(new HashMap<>());
        if (templateField.getCurrentVersion() == null) templateField.setCurrentVersion(0);

        int next = templateField.getCurrentVersion() + 1;
        templateField.setCurrentVersion(next);

        templateField.getVersions().put(String.valueOf(next),
                VersionEntry.builder()
                        .value(newValue)
                        .timeStamp(today)
                        .build()
        );
    }


    public List<ApplicationListResponse> getApplicationsByUserId(String userId) {

        log.info("Fetching applications for user | userId={}", userId);

        List<ApplicationEntity> applications =
                repo.findByUser_Id(userId);

        return applications.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    private ApplicationListResponse toListResponse(ApplicationEntity entity) {
        return ApplicationListResponse.builder()
                .applicationId(entity.getId())
                .name(entity.getName())
                .funderName(entity.getFunderName())
                .status(entity.getStatus())
                .createdDate(entity.getCreateDate())
                .build();
    }


    public ApplicationPayload getApplicationByUserAndId(String userId, String applicationId) {

        log.info("Fetching application details | userId={} applicationId={}", userId, applicationId);

        ApplicationEntity entity = repo.findByIdAndUser_Id(applicationId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "Application not found for userId=" + userId + " applicationId=" + applicationId));

        return ApplicationPayload.builder()
                .applicationId(entity.getId())
                .userId(entity.getUser().getId())
                .name(entity.getName())
                .funderName(entity.getFunderName())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createDate(entity.getCreateDate())
                .fields(entity.getFields())
                .questions(entity.getQuestions())
                .build();


    }
}
