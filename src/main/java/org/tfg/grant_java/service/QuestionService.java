package org.tfg.grant_java.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.controller.QuestionController;
import org.tfg.grant_java.entity.Tenants;
import org.tfg.grant_java.entity.Users;
import org.tfg.grant_java.repository.TenantRepository;
import org.tfg.grant_java.repository.UserRepository;
import org.tfg.grant_java.request.CreateQuestionRequest;
import org.tfg.grant_java.request.RecommendRequest;
import org.tfg.grant_java.request.UpdateQuestionRequest;
import org.tfg.grant_java.response.*;
import org.tfg.grant_java.entity.AnswerVersions;
import org.tfg.grant_java.entity.Questions;
import org.tfg.grant_java.repository.AnswerVersionRepository;
import org.tfg.grant_java.repository.QuestionRepository;
import org.tfg.grant_java.response.CreateQuestionResponse;

@Service
public class QuestionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionRepository questionRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final AnswerVersionRepository answerVersionRepository;
    private final QuestionRecommender questionRecommender;

    public QuestionService(
            QuestionRepository questionRepository,
            TenantRepository tenantRepository,
            UserRepository userRepository,
            AnswerVersionRepository answerVersionRepository,
            QuestionRecommender questionRecommender
    ) {
        this.questionRepository = questionRepository;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.answerVersionRepository = answerVersionRepository;
        this.questionRecommender = questionRecommender;
    }
    @Transactional
    public CreateQuestionResponse create(CreateQuestionRequest req) throws Exception {
        LOGGER.debug("req:{}",req);
        LOGGER.debug("0");
        Tenants tenant = tenantRepository.getReferenceById(req.getTenantId());
        LOGGER.debug("1");
        Users user = userRepository.getReferenceById(req.getUserId());
        LOGGER.debug("2");
        Questions q = Questions.builder()
                .questionId(UUID.randomUUID())
                .tenant(tenant)
                .questionText(req.getQuestion())
                .defaultAnswerId(null)
                .defaultAnswerVersion(null)
                .defaultAnswerText(null)
                .isActive(true)
                .build();
        LOGGER.debug("3");
        questionRepository.save(q);
        LOGGER.debug("4");
        UUID logicalAnswerId = UUID.randomUUID();

        AnswerVersions v1 = AnswerVersions.builder()
                .answerVersionId(UUID.randomUUID())
                .tenant(q.getTenant())
                .question(q) // or questionId FK depending on your mapping
                .answerId(logicalAnswerId)
                .versionNumber(1)
                .answerText(req.getAnswer())
                .createdBy(user)
                .build();
        LOGGER.debug("5");
        answerVersionRepository.save(v1);
        LOGGER.debug("6");
        q.setDefaultAnswerId(logicalAnswerId);
        q.setDefaultAnswerVersion(v1);
        q.setDefaultAnswerText(v1.getAnswerText());
        questionRepository.save(q);
        LOGGER.debug("7");
        questionRecommender.addOrUpdateInIndex(q);
        LOGGER.debug("8");
        return new CreateQuestionResponse(q.getQuestionId());
    }


    @Transactional
    public UpdateQuestionResponse updateQuestion(UUID questionId, UpdateQuestionRequest req) throws Exception {
        LOGGER.debug("questionId:{},req:{}",questionId,req);
        LOGGER.debug("0");
        // Ensure question exists and belongs to tenant
        Questions q = questionRepository.findByQuestionIdAndTenant_TenantId(questionId, req.getTenantId())
                .orElseThrow(() -> new Exception("Question not found for tenant"));
        LOGGER.debug("1");
        // Update question text if provided (you can enforce non-null if you want)
        if (req.getQuestion() != null && !req.getQuestion().isBlank()) {
            q.setQuestionText(req.getQuestion());
        }
        LOGGER.debug("2");
        // Determine next version number
        int nextVersion = answerVersionRepository
                .findTopByQuestion_QuestionIdOrderByVersionNumberDesc(questionId)
                .map(av -> av.getVersionNumber() + 1)
                .orElse(1);
        LOGGER.debug("3");
        // Logical answer id: reuse existing if present, else create new once
//        UUID answerId = (q.getDefaultAnswerId() != null) ? q.getDefaultAnswerId() : UUID.randomUUID();
        UUID answerId = UUID.randomUUID();
        LOGGER.debug("4");
        // Create new answer version row
        AnswerVersions newVer = AnswerVersions.builder()
                .answerVersionId(UUID.randomUUID())
                .tenant(q.getTenant())
                .question(q)
                .answerId(answerId)
                .versionNumber(nextVersion)
                .answerText(req.getAnswer())
                .createdAt(Instant.now())
                .createdBy(userRepository.getReferenceById(req.getUserId()))
                .build();
        LOGGER.debug("5");

        AnswerVersions savedVer = answerVersionRepository.save(newVer);
        q.setDefaultAnswerVersion(savedVer);

        LOGGER.debug("6");
        // Repoint defaults on Question
        q.setDefaultAnswerId(answerId);
//        q.setDefaultAnswerVersion(newVer);
        q.setDefaultAnswerText(newVer.getAnswerText());
        LOGGER.debug("7");
        questionRepository.save(q);
        LOGGER.debug("8");
        LOGGER.debug("9,id:{}",q.getQuestionId());
        questionRecommender.addOrUpdateInIndex(q);
        LOGGER.debug("10");
        UpdateQuestionResponse updateQuestionResponse = new UpdateQuestionResponse(q.getQuestionId());
        LOGGER.debug("updateQuestionResponse:{}",updateQuestionResponse);
        return updateQuestionResponse;
    }


    @Transactional
    public DeleteQuestionResponse deleteQuestion(UUID questionId, UUID tenantId) throws Exception {
        LOGGER.debug("0");
        Questions q = questionRepository.findByQuestionIdAndTenant_TenantId(questionId, tenantId)
                .orElseThrow(() -> new Exception("Question not found for tenant"));
        LOGGER.debug("1");
        // Soft delete (recommended because applications may pin versions)
        q.setActive(false);
        questionRepository.save(q);
        LOGGER.debug("2");
        questionRecommender.deleteFromIndex(questionId.toString());
        DeleteQuestionResponse deleteQuestionResponse = new DeleteQuestionResponse(q.getQuestionId());
        LOGGER.debug("deleteQuestionResponse:{}",deleteQuestionResponse);
        return deleteQuestionResponse;
    }

    public List<Questions> getQuestionsByTenantId(
            UUID tenantId
    ) {
        Page<Questions> page =
                questionRepository.findByTenant_TenantIdAndIsActiveTrue(tenantId, Pageable.unpaged());
        List<Questions> getQuestionsByTenantId = page.getContent();
        LOGGER.debug("getQuestionsByTenantId:{}",getQuestionsByTenantId);
        return getQuestionsByTenantId;
    }

    public GetQuestionsResponse searchQuestions(
        int pageSize,
        String search
    ) throws Exception {
        if(search != null && !search.isBlank()) {
            pageSize = pageSize <= 0 ? 10 : pageSize;
            List<QuestionRecommendation> questionRecommendations = questionRecommender.recommend(new RecommendRequest(search, pageSize));
            List<UUID> questionIds = questionRecommendations.stream().map(o->UUID.fromString(o.getId())).toList();
            List<Questions> questions = questionRepository.findByQuestionIdIn(questionIds);
            List<QuestionSummaryResponse> content = questions.stream().map(this::toSummary).toList();
            GetQuestionsResponse getQuestionsResponse = new GetQuestionsResponse(
                    content,
                    1,
                    pageSize,
                    pageSize,
                    1
            );
            LOGGER.debug("getQuestionsResponse:{}",getQuestionsResponse);
            return getQuestionsResponse;
        } else {
            throw new Exception("No search term provided");
        }
    }
    @Transactional(readOnly = true)
    public GetQuestionsResponse getAllQuestionsForTenant(
            UUID tenantId,
            int pageNumber,
            int pageSize
    ) {
        Pageable pageable;
        if (pageSize <= 0) {
            pageable = Pageable.unpaged();
        } else {
            int zeroBasedPage = Math.max(pageNumber - 1, 0);
            pageable = PageRequest.of(
                    zeroBasedPage,
                    pageSize,
                    Sort.by("createdAt").descending()
            );
        }

        Page<Questions> page =
                questionRepository.findByTenant_TenantIdAndIsActiveTrue(tenantId, pageable);


        List<QuestionSummaryResponse> content =
                page.getContent().stream().map(this::toSummary).toList();
        LOGGER.debug("3");
        LOGGER.debug("questionSummaryResponses:{}",content);
        GetQuestionsResponse response = new GetQuestionsResponse(
            content,
            pageNumber,
            pageSize,
            page.getTotalElements(),
            page.getTotalPages()
        );
        LOGGER.debug("2");
        LOGGER.debug("response:{}",response);
        return response;
    }

    @Transactional(readOnly = true)
    public List<AnswerVersionResponse> getAllVersionsForQuestion(UUID questionId, UUID tenantId) throws Exception {
        LOGGER.debug("2");
        // validate tenant ownership first
        Questions q = questionRepository.findByQuestionIdAndTenant_TenantId(questionId, tenantId)
                .orElseThrow(() -> new Exception("Question not found for tenant"));
        List<AnswerVersions> answerVersionsForQuestion = answerVersionRepository.findByQuestion_QuestionIdOrderByVersionNumberDesc(q.getQuestionId());
        LOGGER.debug("2");
        List<AnswerVersionResponse> answerVersionResponses = answerVersionsForQuestion
            .stream()
            .map(v -> new AnswerVersionResponse(
                    v.getVersionNumber(),
                    v.getAnswerText(),
                    (v.getCreatedBy() == null ? null : v.getCreatedBy().getName()),
                    v.getCreatedAt()
            ))
        .toList();
        LOGGER.debug("2");
        LOGGER.debug("answerVersionResponses:{}",answerVersionResponses);
        return answerVersionResponses;
    }

    private QuestionSummaryResponse toSummary(Questions q) {
        return new QuestionSummaryResponse(
                q.getQuestionId(),
                q.getQuestionText(),
                q.getDefaultAnswerText(),
                (q.getDefaultAnswerVersion() == null ? null : q.getDefaultAnswerVersion().getVersionNumber()),
                q.isActive(),
                q.getCreatedAt()
        );
    }
}