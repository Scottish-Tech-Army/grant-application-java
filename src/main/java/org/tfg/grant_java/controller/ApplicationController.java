package org.tfg.grant_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.ApplicationCreationResponse;
import org.tfg.grant_java.dto.ApplicationDetailsDTO;
import org.tfg.grant_java.dto.GrantApplicationDetails;
import org.tfg.grant_java.dto.GrantApplicationsRequestDTO;
import org.tfg.grant_java.entity.Applications;
import org.tfg.grant_java.service.GrantApplicationService;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private GrantApplicationService grantApplicationService;

    @PostMapping("/v1/delete/{application-id}")
    public ResponseEntity<Void> deleteGrantApplication(@PathVariable("application-id") String applicationId) {
        // Logic to delete the grant application with the given applicationId
        // This could involve calling a service method that interacts with the database
        boolean isDeleted = grantApplicationService.deleteApplication(applicationId);

        if (isDeleted) {
            return ResponseEntity.ok().build(); // Return 204 No Content if deletion is successful
        } else {
            return ResponseEntity.internalServerError().build(); // Return 404 Not Found if the application does not exist
        }
    }

    @GetMapping("/v1/{application-id}")
    public ResponseEntity<ApplicationDetailsDTO> getGrantApplicationDetailsById(@PathVariable("application-id") String applicationId) {
        // Logic to retrieve the grant application details for the given applicationId
        // This could involve calling a service method that interacts with the database
        ApplicationDetailsDTO applicationDetails = grantApplicationService.getApplicationDetails(applicationId);

        if (applicationDetails != null) {
            return ResponseEntity.ok(applicationDetails); // Return 200 OK with the application details
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the application does not exist
        }
    }

    @PostMapping("/v1/createOrUpdate")
    public ResponseEntity<ApplicationCreationResponse> createOrUpdateGrantApplication(@RequestBody ApplicationDetailsDTO applicationDetailsDTO) {
        // Logic to create or update a grant application based on the provided details
        // This could involve calling a service method that interacts with the database
        ApplicationCreationResponse response = grantApplicationService.createOrUpdateApplication(applicationDetailsDTO);

        if (response != null) {
            return ResponseEntity.ok(response); // Return 200 OK with the creation/update response
        } else {
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error if there was an issue processing the request
        }
    }

    @PostMapping("/v1/fetchApplications")
    public ResponseEntity<Page<List<Applications>>> fetchGrantApplications(@RequestBody GrantApplicationsRequestDTO grantApplicationsRequestDTO) {

        Page<List<Applications>> result =
                grantApplicationService.getApplicationsByTenant(grantApplicationsRequestDTO);
        return ResponseEntity.ok(result);
    }
}
