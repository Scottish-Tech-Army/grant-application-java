package org.tfg.grant_java.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.CommonDataDTO;
import org.tfg.grant_java.service.CommonDataService;

import java.util.List;

/**
 * REST controller for managing common data templates.
 *
 * Provides endpoints to retrieve and create common data
 * associated with charities and applications.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/app/common-data")
public class CommonDataController {

    private final CommonDataService commonDataService;

    /**
     * Retrieves common data by identifier.
     *
     * @param id common data identifier
     * @return common data details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonDataDTO> getCommonData(@PathVariable Long id) {

        log.info("Received request to fetch common data | commonDataId={}", id);

        return ResponseEntity.ok(
                commonDataService.getCommonDataById(id)
        );
    }

    /**
     * Retrieves all common data associated with a charity.
     *
     * @param id charity identifier
     * @return list of common data for the charity
     */
    @GetMapping("/charity/{id}")
    public ResponseEntity<List<CommonDataDTO>> getAllCommonDataByCharityId(
            @PathVariable Long id) {

        log.info(
                "Received request to fetch common data by charity | charityId={}",
                id
        );

        return ResponseEntity.ok(
                commonDataService.getAllCommonDataByCharityId(id)
        );
    }

    /**
     * Creates a new common data template.
     *
     * @param commonDataDTO common data input
     * @return generated common data identifier
     */
    @PostMapping
    public ResponseEntity<Long> saveCommonData(
            @RequestBody CommonDataDTO commonDataDTO) {

        log.info(
                "Received request to save common data | charityId={} | version={}",
                commonDataDTO.getCharityId(),
                commonDataDTO.getVersion()
        );

        Long commonDataId = commonDataService.saveCommonData(commonDataDTO);

        log.info(
                "Common data saved successfully | commonDataId={} | charityId={}",
                commonDataId,
                commonDataDTO.getCharityId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commonDataId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommonDataById(@PathVariable Long id) {
        commonDataService.deleteCommonDataById(id);
        return ResponseEntity.ok("Common data deleted");
    }
}
