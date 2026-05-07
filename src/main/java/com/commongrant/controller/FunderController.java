package com.commongrant.controller;

import com.commongrant.dto.*;
import com.commongrant.service.FunderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/funders")
@RequiredArgsConstructor
public class FunderController {

    private final FunderService funderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> list(
            @RequestParam(required = false) String focus,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (focus == null && region == null) {
            return ResponseEntity.ok(ApiResponse.ok(funderService.listAll()));
        }
        Page<FunderDto> result = funderService.search(focus, region,
                PageRequest.of(page, size, Sort.by("name")));
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FunderDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(funderService.getById(id)));
    }
}

