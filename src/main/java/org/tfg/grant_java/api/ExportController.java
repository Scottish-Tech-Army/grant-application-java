package org.tfg.grant_java.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.api.dto.ExportResponse;
import org.tfg.grant_java.service.ExportService;

import java.util.UUID;

@RestController
public class ExportController {
    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/api/orgs/{orgId}/applications/{appId}/export")
    public ResponseEntity<ExportResponse> export(
            @PathVariable UUID orgId,
            @PathVariable UUID appId) {
        return ResponseEntity.ok(exportService.exportApplication(orgId, appId));
    }
}

