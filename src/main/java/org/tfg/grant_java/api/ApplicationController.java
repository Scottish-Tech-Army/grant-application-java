package org.tfg.grant_java.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.api.dto.*;
import org.tfg.grant_java.domain.enums.ApplicationStatus;
import org.tfg.grant_java.service.ApplicationService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private static final UUID DEMO_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody CreateApplicationRequest req) {
        UUID actor = userId != null ? userId : DEMO_USER_ID;
        return new ResponseEntity<>(applicationService.createApplication(req, actor), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> list() {
        return ResponseEntity.ok(applicationService.listApplications());
    }

    @PutMapping("/{appId}/common-selections")
    public ResponseEntity<Void> updateSelections(
            @PathVariable UUID appId,
            @Valid @RequestBody BulkCommonSelectionRequest req) {
        applicationService.updateCommonSelections(appId, req);
        return ResponseEntity.noContent().build();
    }
}
