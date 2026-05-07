package org.tfg.grant_java.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.grant_java.dto.CharityDTO;
import org.tfg.grant_java.service.CharityService;

import java.util.List;

/**
 * REST controller for managing charities.
 *
 * Provides endpoints to retrieve active charities.
 */
@Slf4j
@RestController
@RequestMapping("/app/charities")
@AllArgsConstructor
public class CharityController {

    private final CharityService charityService;

    /**
     * Retrieves all active charities.
     *
     * @return list of active charities
     */
    @GetMapping
    public List<CharityDTO> getAllActiveCharities() {

        log.info("Received request to fetch all active charities");

        return charityService.getAllActiveCharities();
    }
}