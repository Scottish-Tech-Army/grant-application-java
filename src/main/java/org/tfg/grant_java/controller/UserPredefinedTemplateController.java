package org.tfg.grant_java.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.UpsertPredefinedItemRequest;
import org.tfg.grant_java.dto.UserPredefinedTemplateResponse;
import org.tfg.grant_java.service.UserPredefinedTemplateService;

@RestController

@RequestMapping("/api/v1/users")
@Tag(name = "User Predefined Templates", description = "Fetch predefined fields and questions for a user")
public class UserPredefinedTemplateController {

    private final UserPredefinedTemplateService service;

    @Autowired
    public UserPredefinedTemplateController(UserPredefinedTemplateService service) {
        this.service = service;
    }

    @Operation(
            summary = "Fetch user predefined fields and questions",
            description = "Returns all predefined fields and questions for the given userId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Templates fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid userId"),
            @ApiResponse(responseCode = "404", description = "Templates not found for user")
    })
    @GetMapping("/{userId}/predefined")
    public UserPredefinedTemplateResponse getUserPredefinedTemplates(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {

        return service.fetchUserPredefinedTemplates(userId);
    }


    @Operation(summary = "Upsert predefined FIELD (create user row if missing, append field if missing, add new version)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upsert successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{userId}/predefined/fields")
    public ResponseEntity<Void> upsertField(
            @Parameter(required = true) @PathVariable String userId,
            @Valid @RequestBody UpsertPredefinedItemRequest request) {

        service.upsertField(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upsert predefined QUESTION (create user row if missing, append question if missing, add new version)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upsert successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{userId}/predefined/questions")
    public ResponseEntity<Void> upsertQuestion(
            @Parameter(required = true) @PathVariable String userId,
            @Valid @RequestBody UpsertPredefinedItemRequest request) {

        service.upsertQuestion(userId, request);
        return ResponseEntity.ok().build();
    }

}
