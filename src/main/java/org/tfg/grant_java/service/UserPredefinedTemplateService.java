package org.tfg.grant_java.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.UpsertPredefinedItemRequest;
import org.tfg.grant_java.dto.UserPredefinedTemplateResponse;
import org.tfg.grant_java.entity.UserEntity;
import org.tfg.grant_java.entity.UserPredefinedFieldsEntity;
import org.tfg.grant_java.entity.UserPredefinedQuestionsEntity;
import org.tfg.grant_java.model.VersionEntry;
import org.tfg.grant_java.model.VersionedItem;
import org.tfg.grant_java.repository.UserPredefinedFieldsRepository;
import org.tfg.grant_java.repository.UserPredefinedQuestionsRepository;
import org.tfg.grant_java.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class UserPredefinedTemplateService {

    private final UserPredefinedFieldsRepository fieldsRepository;
    private final UserPredefinedQuestionsRepository questionsRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserPredefinedTemplateService(UserPredefinedFieldsRepository fieldsRepository, UserPredefinedQuestionsRepository questionsRepository, UserRepository userRepository) {
        this.fieldsRepository = fieldsRepository;
        this.questionsRepository = questionsRepository;
        this.userRepository = userRepository;
    }

    public UserPredefinedTemplateResponse fetchUserPredefinedTemplates(String userId) {

        log.info("Fetching predefined fields & questions | userId={}", userId);

        UserPredefinedFieldsEntity fieldsEntity =
                fieldsRepository.findByUser_Id(userId)
                        .orElseThrow(() -> {
                            log.warn("Predefined fields not found  userId={}", userId);
                            return new RuntimeException("Predefined fields not found for userId=" + userId);
                        });

        UserPredefinedQuestionsEntity questionsEntity =
                questionsRepository.findByUser_Id(userId)
                        .orElseThrow(() -> {
                            log.warn("Predefined questions not found  userId={}", userId);
                            return new RuntimeException("Predefined questions not found for userId=" + userId);
                        });

        log.info("Predefined templates fetched successfully | userId={} fieldsCount={} questionsCount={}",
                userId,
                fieldsEntity.getFields().size(),
                questionsEntity.getQuestions().size()
        );

        return UserPredefinedTemplateResponse.builder()
                .userId(userId)
                .fields(fieldsEntity.getFields())
                .questions(questionsEntity.getQuestions())
                .build();
    }


    @Transactional
    public void upsertField(String userId, UpsertPredefinedItemRequest req) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        String today = LocalDate.now().toString();
        UserPredefinedFieldsEntity entity = fieldsRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    log.info("Creating predefined fields row for user | userId={}", userId);
                    return UserPredefinedFieldsEntity.builder()
                            .user(user)
                            .createdDate(today)
                            .fields(new ArrayList<>())
                            .build();
                });

        if (entity.getFields() == null) entity.setFields(new ArrayList<>());

        VersionedItem item = entity.getFields().stream()
                .filter(f -> f.getFieldKey().equalsIgnoreCase(req.getFieldKey()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            // create a new VersionedItem and append
            item = VersionedItem.builder()
                    .fieldKey(req.getFieldKey())
                    .type("PREDEFINED")
                    .currentVersion(0)
                    .versions(new HashMap<>())
                    .build();
            entity.getFields().add(item);
        }

        // If value already exists in versions -> do nothing
        if (valueExists(item, req.getValue())) {
            log.info("No-op (value already exists) | userId={} fieldKey={}", userId, req.getFieldKey());
            fieldsRepository.save(entity);
            return;
        }

        appendNewVersion(item, req.getValue());

        // converter-based JSON: force dirty tracking
        entity.setFields(new ArrayList<>(entity.getFields()));

        fieldsRepository.save(entity);
        log.info("Upserted predefined field | userId={} fieldKey={} newVersion={}",
                userId, req.getFieldKey(), item.getCurrentVersion());
    }

    // ---------- QUESTIONS UPSERT ----------
    @Transactional
    public void upsertQuestion(String userId, UpsertPredefinedItemRequest req) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        String today = LocalDate.now().toString();
        UserPredefinedQuestionsEntity entity = questionsRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    log.info("Creating predefined questions row for user | userId={}", userId);
                    return UserPredefinedQuestionsEntity.builder()
                            .user(user)
                            .createdDate(today)
                            .questions(new ArrayList<>())
                            .build();
                });

        if (entity.getQuestions() == null) entity.setQuestions(new ArrayList<>());

        VersionedItem item = entity.getQuestions().stream()
                .filter(q -> q.getFieldKey().equalsIgnoreCase(req.getFieldKey()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = VersionedItem.builder()
                    .fieldKey(req.getFieldKey())
                    .type("PREDEFINED")
                    .currentVersion(0)
                    .versions(new HashMap<>())
                    .build();
            entity.getQuestions().add(item);
        }

        if (valueExists(item, req.getValue())) {
            log.info("No-op (value already exists) | userId={} questionKey={}", userId, req.getFieldKey());
            questionsRepository.save(entity);
            return;
        }

        appendNewVersion(item, req.getValue());

        entity.setQuestions(new ArrayList<>(entity.getQuestions()));
        questionsRepository.save(entity);

        log.info("Upserted predefined question | userId={} questionKey={} newVersion={}",
                userId, req.getFieldKey(), item.getCurrentVersion());
    }

    // ---------- Helpers ----------
    private boolean valueExists(VersionedItem item, Object value) {
        if (item.getVersions() == null) return false;
        return item.getVersions().values().stream().anyMatch(v -> Objects.equals(v.getValue(), value));
    }

    private void appendNewVersion(VersionedItem item, Object newValue) {
        if (item.getVersions() == null) item.setVersions(new HashMap<>());
        if (item.getCurrentVersion() == null) item.setCurrentVersion(0);

        int next = item.getCurrentVersion() + 1;
        item.setCurrentVersion(next);

        String today = LocalDate.now().toString();
        item.getVersions().put(String.valueOf(next),
                VersionEntry.builder()
                        .value(newValue)
                        .timeStamp(today)
                        .build());
    }

}
