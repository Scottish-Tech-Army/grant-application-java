package org.tfg.grant_java.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.api.dto.CreateCustomQuestionRequest;
import org.tfg.grant_java.api.dto.CustomQuestionResponse;
import org.tfg.grant_java.domain.ApplicationCustomQuestion;
import org.tfg.grant_java.domain.FundingApplication;
import org.tfg.grant_java.exception.BadRequestException;
import org.tfg.grant_java.exception.NotFoundException;
import org.tfg.grant_java.repo.ApplicationCustomQuestionRepository;
import org.tfg.grant_java.repo.FundingApplicationRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomQuestionService {
    private final ApplicationCustomQuestionRepository questionRepository;
    private final FundingApplicationRepository applicationRepository;

    public CustomQuestionService(ApplicationCustomQuestionRepository questionRepository,
                                 FundingApplicationRepository applicationRepository) {
        this.questionRepository = questionRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public CustomQuestionResponse addCustomQuestion(UUID appId, CreateCustomQuestionRequest req) {
        FundingApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
        ApplicationCustomQuestion q = new ApplicationCustomQuestion();
        q.setApplication(app);
        q.setQuestionText(req.getQuestionText());
        q.setAnswerText(req.getAnswerText());
        q.setDisplayOrder(req.getDisplayOrder());
        q.setCreatedAt(Instant.now());
        q.setUpdatedAt(Instant.now());
        ApplicationCustomQuestion saved = questionRepository.save(q);
        return toResponse(saved);
    }

    public List<CustomQuestionResponse> listCustomQuestions(UUID appId) {
        FundingApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
        return questionRepository.findByApplicationIdOrderByDisplayOrderAsc(appId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public CustomQuestionResponse updateCustomQuestion(UUID questionId, CreateCustomQuestionRequest req) {
        ApplicationCustomQuestion q = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Custom question not found"));
        q.setQuestionText(req.getQuestionText());
        q.setAnswerText(req.getAnswerText());
        q.setDisplayOrder(req.getDisplayOrder());
        q.setUpdatedAt(Instant.now());
        ApplicationCustomQuestion saved = questionRepository.save(q);
        return toResponse(saved);
    }

    @Transactional
    public void deleteCustomQuestion(UUID questionId) {
        ApplicationCustomQuestion q = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Custom question not found"));
        questionRepository.delete(q);
    }

    private CustomQuestionResponse toResponse(ApplicationCustomQuestion q) {
        CustomQuestionResponse dto = new CustomQuestionResponse();
        dto.setId(q.getId());
        dto.setQuestionText(q.getQuestionText());
        dto.setAnswerText(q.getAnswerText());
        dto.setDisplayOrder(q.getDisplayOrder());
        return dto;
    }
}
