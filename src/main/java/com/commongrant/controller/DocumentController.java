package com.commongrant.controller;

import com.commongrant.dto.*;
import com.commongrant.security.OrgUserDetails;
import com.commongrant.service.DocumentVaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentVaultService docService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VaultDocumentDto>>> list(
            @AuthenticationPrincipal OrgUserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok(docService.listByOrg(user.getOrgId())));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<VaultDocumentDto>> upload(
            @AuthenticationPrincipal OrgUserDetails user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "expiresAt", required = false) String expiresAt) throws IOException {

        VaultDocumentDto doc = docService.upload(user.getOrgId(), file, documentType, expiresAt);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Uploaded successfully", doc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal OrgUserDetails user,
            @PathVariable Long id) {
        docService.delete(id, user.getOrgId());
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}

