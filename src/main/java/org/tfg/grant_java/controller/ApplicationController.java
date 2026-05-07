package org.tfg.grant_java.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.ApplicationListResponse;
import org.tfg.grant_java.dto.ApplicationPayload;
import org.tfg.grant_java.service.ApplicationService;
import org.tfg.grant_java.service.PdfExportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applications")
@Tag(
        name = "Applications",
        description = "APIs to create and submit grant applications"
)
public class ApplicationController {

    private final ApplicationService applicationService;


    private final PdfExportService pdfExportService;


    @Operation(
            summary = "Submit application",
            description = "Submits an application and reconciles any new predefined field/question versions before persisting"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/submit")
    public ResponseEntity<Void> submitApplication(
            @Parameter(description = "Application submit payload", required = true)
            @RequestBody ApplicationPayload request) {

        applicationService.create(request);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "List applications for a user",
            description = "Returns all applications created by the given userId"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationListResponse>> getApplicationsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {

        List<ApplicationListResponse> applications =
                applicationService.getApplicationsByUserId(userId);

        return ResponseEntity.ok(applications);
    }


    @Operation(
            summary = "Get specific application by userId and applicationId",
            description = "Returns full application payload (same as submit payload) along with applicationId"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Application not found for this user")
    })
    @GetMapping("/{userId}/applications/{applicationId}")
    public ResponseEntity<ApplicationPayload> getApplication(
            @Parameter(required = true) @PathVariable String userId,
            @Parameter(required = true) @PathVariable String applicationId) {

        return ResponseEntity.ok(applicationService.getApplicationByUserAndId(userId, applicationId));
    }



    @Operation(summary = "Download application PDF")
    @GetMapping("/{userId}/applications/{applicationId}/export/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String userId,
            @PathVariable String applicationId) {

        byte[] pdf = pdfExportService.exportApplicationPdf(userId, applicationId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=application-" + applicationId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }



}
