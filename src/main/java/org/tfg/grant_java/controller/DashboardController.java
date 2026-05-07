package org.tfg.grant_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.DashboardRequestDto;
import org.tfg.grant_java.dto.DashboardResponseDto;
import org.tfg.grant_java.service.DashboardService;

@RestController
@CrossOrigin
@RequestMapping("/v1/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @PostMapping()
    public ResponseEntity<DashboardResponseDto> getDashboard(@RequestBody DashboardRequestDto dashboardRequestDto) {
        return ResponseEntity.ok(dashboardService.getDashboard(dashboardRequestDto));
    }
}
