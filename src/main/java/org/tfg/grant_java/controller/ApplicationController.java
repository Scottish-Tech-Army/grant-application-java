package org.tfg.grant_java.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.ApplicationDTO;
import org.tfg.grant_java.model.ApplicationJsonDataResponse;
import org.tfg.grant_java.model.ApplicationResponse;
import org.tfg.grant_java.service.ApplicationService;

import java.util.List;

/**
 * REST controller for managing grant applications.
 *
 * Provides endpoints to create applications and
 * retrieve application details for a charity.
 */
@Slf4j
@RestController
@RequestMapping("/app/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * Retrieves a single application along with its filtered common data.
     *
     * @param id the application identifier
     * @return application data and associated common data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationJsonDataResponse> getApplication(
            @PathVariable Long id) {

        log.info("Received request to fetch application | applicationId={}", id);

        return ResponseEntity.ok(
                applicationService.getApplication(id)
        );
    }

    /**
     * Retrieves all applications for a given charity.
     *
     * @param charityId the charity identifier
     * @return list of applications for the charity
     */
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByCharity(
            @RequestParam Long charityId) {

        log.info(
                "Received request to fetch applications by charity | charityId={}",
                charityId
        );

        return ResponseEntity.ok(
                applicationService.getApplicationsByCharity(charityId).stream()
                        .map(application -> {
                            ApplicationResponse applicationResponse = new ApplicationResponse();
                            BeanUtils.copyProperties(application, applicationResponse);
                            return applicationResponse;
                        }).toList()
        );
    }

    /**
     * Creates a new application for a charity.
     *
     * @param applicationDTO application input data
     * @return generated application identifier
     */
    @PostMapping
    public ResponseEntity<Long> createAndUpdateApplication(@RequestHeader("Authorization") String token,
            @RequestBody ApplicationDTO applicationDTO) {

        log.info(
                "Received request to create application | charityId={}",
                applicationDTO.getCharityId()
        );

        Long applicationId = applicationService.createApplication(applicationDTO);

        log.info(
                "Application created successfully | applicationId={} | charityId={}",
                applicationId,
                applicationDTO.getCharityId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok("Application deleted");
    }

}