package org.tfg.grant_java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.request.CreateQuestionRequest;
import org.tfg.grant_java.request.RecommendRequest;
import org.tfg.grant_java.request.UpdateQuestionRequest;
import org.tfg.grant_java.response.*;
import org.tfg.grant_java.service.QuestionRecommender;
import org.tfg.grant_java.service.QuestionService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/question")
public class QuestionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;
    private final QuestionRecommender questionRecommender;

    public QuestionController(QuestionService questionService, QuestionRecommender questionRecommenderService) {
        this.questionService = questionService;
        this.questionRecommender = questionRecommenderService;
    }

    @PostMapping
    public ResponseEntity<CreateQuestionResponse> create(@RequestBody CreateQuestionRequest questionCreateRequest) throws Exception {
        CreateQuestionResponse createQuestionResponse = questionService.create(questionCreateRequest);
        return ResponseEntity.ok(createQuestionResponse);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<UpdateQuestionResponse> update(
            @PathVariable UUID questionId,
            @RequestBody UpdateQuestionRequest req
    ) throws Exception {
        UpdateQuestionResponse updateQuestionResponse = questionService.updateQuestion(questionId, req);
        return ResponseEntity.ok(updateQuestionResponse);
    }


    // DELETE (soft delete)
    @DeleteMapping("/{questionId}")
    public ResponseEntity<DeleteQuestionResponse> delete(
            @PathVariable UUID questionId,
            @RequestParam UUID tenantId
    ) throws Exception {
        return ResponseEntity.ok(questionService.deleteQuestion(questionId, tenantId));
    }

    // GET ALL QUESTIONS FOR TENANT
    @GetMapping
    public ResponseEntity<GetQuestionsResponse> getAllForTenant(
            @RequestParam UUID tenantId,
            @RequestParam(defaultValue = "1") int pageNumber,   // ✅ 1‑indexed
            @RequestParam(defaultValue = "10") int pageSize
    ) throws Exception {
        return ResponseEntity.ok(questionService.getAllQuestionsForTenant(tenantId, pageNumber, pageSize));
    }

    // GET ALL VERSIONS FOR A QUESTION
    @GetMapping("/{questionId}/versions")
    public ResponseEntity<List<AnswerVersionResponse>> getVersions(
            @PathVariable UUID questionId,
            @RequestParam UUID tenantId
    ) throws Exception {
        return ResponseEntity.ok(questionService.getAllVersionsForQuestion(questionId, tenantId));
    }


    //  curl -X POST http://localhost:8080/v1/question/recommendations -H "Content-Type: application/json" -d "{\"questionText\":\"objective\", \"topN\":5}"
    @PostMapping("/recommendations")
    public ResponseEntity<GetQuestionsResponse> recommend(@RequestBody RecommendRequest recommendRequest) throws Exception {
        LOGGER.trace("Initialized question recommend");
        LOGGER.trace("recommendRequest:{}", recommendRequest);
//        List<QuestionRecommendation> recommendations = questionRecommender.recommend(recommendRequest);
        GetQuestionsResponse recommendations = questionService.searchQuestions(recommendRequest.getTopN(), recommendRequest.getQuestionText());
        LOGGER.trace("recommendations:{}", recommendations);
        LOGGER.trace("Terminated question recommend");
        return ResponseEntity.ok(recommendations);
    }
}
