package com.commongrant.controller;

import com.commongrant.dto.*;
import com.commongrant.security.OrgUserDetails;
import com.commongrant.service.ComplianceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService complianceService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<ComplianceOverviewDto>> overview(
            @AuthenticationPrincipal OrgUserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok(complianceService.getOverview(user.getOrgId())));
    }

    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<String>> submitReport(
            @AuthenticationPrincipal OrgUserDetails user,
            @Valid @RequestBody SubmitReportRequest req) {
        complianceService.submitReport(user.getOrgId(), req);
        return ResponseEntity.ok(ApiResponse.ok("Report submitted successfully", null));
    }
}

