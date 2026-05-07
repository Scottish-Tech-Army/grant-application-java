package org.tfg.grant_java.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.api.dto.*;
import org.tfg.grant_java.service.CommonFieldService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/common-fields")
public class CommonFieldController {
    private final CommonFieldService commonFieldService;
    private static final UUID DEMO_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public CommonFieldController(CommonFieldService commonFieldService) {
        this.commonFieldService = commonFieldService;
    }

    @GetMapping
    public ResponseEntity<List<CommonFieldResponse>> list() {
        return ResponseEntity.ok(commonFieldService.listCommonFields());
    }

    @PostMapping
    public ResponseEntity<CommonFieldResponse> create(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody CreateCommonFieldRequest req) {
        UUID actor = userId != null ? userId : DEMO_USER_ID;
        return new ResponseEntity<>(commonFieldService.createCommonField(req, actor), HttpStatus.CREATED);
    }

    @PostMapping("/{fieldId}/versions")
    public ResponseEntity<CommonFieldResponse> addVersion(
            @PathVariable UUID fieldId,
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody AddCommonFieldVersionRequest req) {
        UUID actor = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(commonFieldService.addNewVersion(fieldId, req, actor));
    }

    @GetMapping("/{fieldId}/versions")
    public ResponseEntity<List<CommonFieldVersionResponse>> getVersions(
            @PathVariable UUID fieldId) {
        return ResponseEntity.ok(commonFieldService.getVersions(fieldId));
    }
}
