package org.tfg.grant_java.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.CommonDataDTO;
import org.tfg.grant_java.entity.CommonData;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.CommonDataRepository;
import org.tfg.grant_java.service.CommonDataService;

import java.util.List;

/**
 * Default implementation of {@link CommonDataService}.
 *
 * <p>This service manages reusable common data templates stored as JSON.
 * These templates are charity-scoped and can be shared across multiple
 * grant applications.</p>
 *
 * <p><b>Main responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve common data by identifier</li>
 *   <li>List common data for a specific charity</li>
 *   <li>Bulk-fetch common data by IDs</li>
 *   <li>Validate and persist common data JSON</li>
 * </ul>
 *
 * <p><b>JSON handling:</b></p>
 * <ul>
 *   <li>Expects the input JSON to be an array</li>
 *   <li>Automatically injects sequential {@code id} fields into each JSON object</li>
 *   <li>Pretty-prints JSON before persisting</li>
 * </ul>
 */
@Slf4j
@AllArgsConstructor
@Service
public class CommonDataServiceImpl implements CommonDataService {

    /**
     * Repository used for CRUD operations on {@link CommonData} entities.
     */
    private final CommonDataRepository commonDataRepository;

    /**
     * Retrieves common data by its unique identifier.
     *
     * @param id unique identifier of the common data
     * @return common data DTO
     * @throws ServiceException if the common data does not exist
     */
    @Override
    public CommonDataDTO getCommonDataById(Long id) {

        log.info("Fetching common data | commonDataId={}", id);

        return commonDataRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> {
                    log.warn("Common data not found | commonDataId={}", id);
                    return new ServiceException(Constants.COMMON_DATA_ID_NOT_FOUND);
                });
    }

    /**
     * Retrieves all common data records associated with a specific charity.
     *
     * @param id identifier of the charity
     * @return list of common data DTOs
     */
    @Override
    public List<CommonDataDTO> getAllCommonDataByCharityId(Long id) {

        log.info("Fetching common data by charity | charityId={}", id);

        List<CommonDataDTO> result = commonDataRepository.findByCharityIdAndIsActiveTrue(id)
                .stream()
                .map(this::mapToDto)
                .toList();

        log.info(
                "Common data fetched by charity | charityId={} | count={}",
                id,
                result.size()
        );

        return result;
    }

    /**
     * Retrieves common data records matching the given list of identifiers.
     *
     * @param ids list of common data identifiers
     * @return list of matching common data DTOs
     */
    @Override
    public List<CommonDataDTO> getAllCommonDataByListOfIds(List<Long> ids) {

        log.info("Fetching common data by ids | idsCount={}", ids.size());

        return commonDataRepository.findByIdIn(ids)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Persists a new common data record.
     *
     * <p>This method validates the JSON payload, injects sequential
     * {@code id} fields into each array element, and stores the
     * formatted JSON.</p>
     *
     * @param commonDataDTO data required to create the common data record
     * @return identifier of the newly created common data
     * @throws ServiceException if the JSON payload is invalid
     */
    @Override
    public Long saveCommonData(CommonDataDTO commonDataDTO) {

        log.info(
                "Saving common data | charityId={} | version={}",
                commonDataDTO.getCharityId(),
                commonDataDTO.getVersion()
        );

        ObjectMapper mapper = new ObjectMapper();

        try {
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(commonDataDTO.getDataJson());

            log.debug(
                    "Processing common data JSON | charityId={} | itemsCount={}",
                    commonDataDTO.getCharityId(),
                    arrayNode.size()
            );

            int sequence = 1;
            for (JsonNode node : arrayNode) {
                ((ObjectNode) node).put("id", sequence++);
            }

            commonDataDTO.setDataJson(
                    mapper.writeValueAsString(arrayNode)
            );

        } catch (JsonProcessingException e) {

            log.warn(
                    "Invalid common data JSON | charityId={} | version={} | reason={}",
                    commonDataDTO.getCharityId(),
                    commonDataDTO.getVersion(),
                    e.getOriginalMessage()
            );

            throw new ServiceException(Constants.COMMON_DATA_ID_NOT_FOUND);
        }

        Long id = commonDataRepository.save(mapToEntity(commonDataDTO)).getId();

        log.info(
                "Common data saved | commonDataId={} | charityId={}",
                id,
                commonDataDTO.getCharityId()
        );

        return id;
    }

    @Override
    public void deleteCommonDataById(Long id) {
        if (!commonDataRepository.existsByIdAndIsActiveTrue(id)) {
            throw new ServiceException(Constants.COMMON_DATA_ID_NOT_FOUND);
        }
        commonDataRepository.updateIsActive(id, false);
    }

    /**
     * Maps a {@link CommonData} entity to {@link CommonDataDTO}.
     *
     * @param commonData common data entity
     * @return mapped DTO
     */
    private CommonDataDTO mapToDto(CommonData commonData) {
        CommonDataDTO dto = new CommonDataDTO();
        dto.setId(commonData.getId());
        dto.setCharityId(commonData.getCharityId());
        dto.setVersion(commonData.getVersion());
        dto.setDataJson(commonData.getDataJson());
        dto.setIsActive(commonData.getIsActive());
        dto.setTemplateTitle(commonData.getTemplateTitle());
        dto.setDescription(commonData.getDescription());
        dto.setCreatedAt(commonData.getCreatedAt());
        dto.setCreatedBy(commonData.getCreatedBy());
        return dto;
    }

    /**
     * Maps a {@link CommonDataDTO} to a {@link CommonData} entity.
     *
     * @param commonDataDTO common data DTO
     * @return mapped entity
     */
    private CommonData mapToEntity(CommonDataDTO commonDataDTO) {
        CommonData entity = new CommonData();
        entity.setCharityId(commonDataDTO.getCharityId());
        entity.setVersion("v"+ commonDataDTO.getVersion());
        entity.setDataJson(commonDataDTO.getDataJson());
        entity.setDescription(commonDataDTO.getDescription());
        entity.setTemplateTitle(commonDataDTO.getTemplateTitle());
        entity.setCreatedBy(commonDataDTO.getCreatedBy());
        return entity;
    }
}