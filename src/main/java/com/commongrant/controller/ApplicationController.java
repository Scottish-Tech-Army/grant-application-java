package com.commongrant.controller;

import com.commongrant.dto.*;
import com.commongrant.security.OrgUserDetails;
import com.commongrant.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService appService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationSummaryDto>>> list(
            @AuthenticationPrincipal OrgUserDetails user,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ApiResponse.ok(appService.listByOrg(user.getOrgId(), status)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDetailDto>> getById(
            @AuthenticationPrincipal OrgUserDetails user,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(appService.getById(id, user.getOrgId())));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> dashboard(
            @AuthenticationPrincipal OrgUserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok(appService.getDashboard(user.getOrgId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationDetailDto>> create(
            @AuthenticationPrincipal OrgUserDetails user,
            @Valid @RequestBody CreateApplicationRequest req) {
        ApplicationDetailDto created = appService.create(user.getOrgId(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Application created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDetailDto>> update(
            @AuthenticationPrincipal OrgUserDetails user,
            @PathVariable Long id,
            @RequestBody UpdateApplicationRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(appService.update(id, user.getOrgId(), req)));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<SubmitResultDto>> submit(
            @AuthenticationPrincipal OrgUserDetails user,
            @PathVariable Long id,
            @RequestBody SubmitRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Submitted successfully",
                appService.submit(id, user.getOrgId(), req.getFunderIds())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal OrgUserDetails user,
            @PathVariable Long id) {
        appService.delete(id, user.getOrgId());
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}

