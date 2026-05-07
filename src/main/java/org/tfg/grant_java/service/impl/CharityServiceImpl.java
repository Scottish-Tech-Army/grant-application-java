package org.tfg.grant_java.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.CharityDTO;
import org.tfg.grant_java.entity.Charity;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.CharityRepository;
import org.tfg.grant_java.service.CharityService;

import java.util.List;

/**
 * Default implementation of {@link CharityService}.
 *
 * <p>This service provides read-only operations for charity data and
 * enforces business rules related to charity availability and active status.</p>
 *
 * <p><b>Main responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve all active charities from the system</li>
 *   <li>Convert {@link Charity} entities into {@link CharityDTO} objects</li>
 *   <li>Handle error scenarios when no active charities are available</li>
 * </ul>
 */
@Slf4j
@AllArgsConstructor
@Service
public class CharityServiceImpl implements CharityService {

    /**
     * Repository used for accessing {@link Charity} entities.
     */
    private final CharityRepository charityRepository;

    /**
     * Retrieves all active charities.
     *
     * <p>This method queries the repository for charities marked as active.
     * If no active charities are found, a business exception is raised.</p>
     *
     * @return list of active charity DTOs
     * @throws ServiceException if no active charities are available
     */
    @Override
    public List<CharityDTO> getAllActiveCharities() {

        log.info("Fetching all active charities");

        List<Charity> charities = charityRepository.findAllByIsActiveTrue()
                .orElseThrow(() -> {
                    log.warn("No active charities found");
                    return new ServiceException(Constants.NO_ACTIVE_CHARITIES_AVAILABLE);
                });

        log.info("Active charities fetched | count={}", charities.size());

        return charities.stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Maps a {@link Charity} entity to {@link CharityDTO}.
     *
     * @param charity charity entity
     * @return mapped charity DTO
     */
    private CharityDTO mapToDto(Charity charity) {

        CharityDTO charityDTO = new CharityDTO();
        charityDTO.setId(charity.getId());
        charityDTO.setName(charity.getName());
        charityDTO.setIsActive(charity.getIsActive());
        charityDTO.setCreatedBy(charity.getCreatedBy());
        return charityDTO;
    }
}