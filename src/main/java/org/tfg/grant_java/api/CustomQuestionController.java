package org.tfg.grant_java.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.api.dto.*;
import org.tfg.grant_java.service.CustomQuestionService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class CustomQuestionController {
    private final CustomQuestionService customQuestionService;

    public CustomQuestionController(CustomQuestionService customQuestionService) {
        this.customQuestionService = customQuestionService;
    }

    @PostMapping("/api/applications/{appId}/custom-questions")
    public ResponseEntity<CustomQuestionResponse> addCustomQuestion(
            @PathVariable UUID appId,
            @Valid @RequestBody CreateCustomQuestionRequest req) {
        return new ResponseEntity<>(customQuestionService.addCustomQuestion(appId, req), HttpStatus.CREATED);
    }

    @GetMapping("/api/applications/{appId}/custom-questions")
    public ResponseEntity<List<CustomQuestionResponse>> listCustomQuestions(
            @PathVariable UUID appId) {
        return ResponseEntity.ok(customQuestionService.listCustomQuestions(appId));
    }

    @PutMapping("/api/custom-questions/{questionId}")
    public ResponseEntity<CustomQuestionResponse> updateCustomQuestion(
            @PathVariable UUID questionId,
            @Valid @RequestBody CreateCustomQuestionRequest req) {
        return ResponseEntity.ok(customQuestionService.updateCustomQuestion(questionId, req));
    }

    @DeleteMapping("/api/custom-questions/{questionId}")
    public ResponseEntity<Void> deleteCustomQuestion(
            @PathVariable UUID questionId) {
        customQuestionService.deleteCustomQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
