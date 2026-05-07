package org.tfg.grant_java.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.ApplicationDTO;
import org.tfg.grant_java.entity.Application;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.ApplicationJsonDataResponse;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.ApplicationRepository;
import org.tfg.grant_java.service.ApplicationService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Default implementation of {@link ApplicationService}.
 *
 * <p>This service coordinates application persistence via {@link ApplicationRepository}
 * and enriches application responses with charity-scoped common data templates obtained
 * from {@link CommonDataServiceImpl}.</p> 
 *
 * <p><b>Main responsibilities:</b></p>
 * <ul>
 *   <li>Fetch a single application and build an aggregated {@link ApplicationJsonDataResponse}</li>
 *   <li>List applications by charity and list all applications</li>
 *   <li>Create new applications</li>
 * </ul>
 *
 * <p><b>Common data filtering:</b> When building {@link #getApplication(Long)}, this service
 * parses the common data JSON (expected to be a JSON array of objects each containing an {@code id})
 * and filters it to only include the IDs listed in {@code selectedCommonKeys} (e.g. {@code [1,2]}).</p> 
 *
 * <p><b>Error handling:</b></p>
 * <ul>
 *   <li>Throws {@link ServiceException} with {@link Constants#APPLICATION_ID_NOT_FOUND}
 *       if the application does not exist</li>
 *   <li>Throws {@link ServiceException} with {@link Constants#COMMON_DATA_JSON_INVALID}
 *       if the common data JSON cannot be parsed</li>
 * </ul> 
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    /**
     * Repository used for CRUD operations on {@link Application} entities.
     */
    private final ApplicationRepository repository;

    /**
     * Service used to retrieve common/template data associated with an application.
     */
    private final CommonDataServiceImpl commonDataService;

    /**
     * Retrieves an application by id and returns a response containing:
     * application data JSON + filtered common data JSON + template metadata.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Loads the {@link Application} record and maps it to {@link ApplicationDTO}</li>
     *   <li>Loads common data via {@code commonDataId}</li>
     *   <li>Parses {@code selectedCommonKeys} and filters the common data JSON array
     *       to only include matching objects by their {@code id}</li>
     *   <li>Builds and returns {@link ApplicationJsonDataResponse}</li>
     * </ol> 
     *
     * @param id unique identifier of the application
     * @return aggregated application response with filtered common data
     * @throws ServiceException when the application is missing or common data JSON is invalid
     */
    @Override
    public ApplicationJsonDataResponse getApplication(Long id) {

        log.info("Fetching application details | applicationId={}", id);

        var applicationDto = repository.findByIdAndIsActiveTrue(id)
                .map(this::mapToDto)
                .orElseThrow(() -> {
                    log.warn("Application not found | applicationId={}", id);
                    return new ServiceException(Constants.APPLICATION_ID_NOT_FOUND);
                });

        log.debug(
                "Application record loaded | applicationId={} | charityId={} | commonDataId={} | selectedCommonKeys={}",
                applicationDto.getId(),
                applicationDto.getCharityId(),
                applicationDto.getCommonDataId(),
                applicationDto.getSelectedCommonKeys()
        );

        var commonDataDTO = commonDataService.getCommonDataById(applicationDto.getCommonDataId());

        log.debug(
                "Common data loaded | commonDataId={} | charityId={} | version={} | templateTitle={}",
                commonDataDTO.getId(),
                commonDataDTO.getCharityId(),
                commonDataDTO.getVersion(),
                commonDataDTO.getTemplateTitle()
        );

        String commonDataJson;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(commonDataDTO.getDataJson());

            Set<Integer> allowedIds = Arrays.stream(
                            applicationDto.getSelectedCommonKeys()
                                    .substring(1, applicationDto.getSelectedCommonKeys().length() - 1)
                                    .split(",")
                    )
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            log.debug(
                    "Filtering common data JSON | applicationId={} | allowedIdsCount={}",
                    id, allowedIds.size()
            );

            List<JsonNode> filteredList =
                    StreamSupport.stream(jsonNode.spliterator(), false)
                            .filter(n -> n.has("id") && allowedIds.contains(n.get("id").asInt()))
                            .toList();

            ArrayNode result = objectMapper.valueToTree(filteredList);
            commonDataJson = objectMapper.writeValueAsString(result);

            log.info(
                    "Application details prepared | applicationId={} | commonItemsSelected={}",
                    id, filteredList.size()
            );

        } catch (JsonProcessingException e) {
            log.warn(
                    "Invalid common data JSON while preparing application | applicationId={} | commonDataId={} | reason={}",
                    id,
                    applicationDto.getCommonDataId(),
                    e.getOriginalMessage()
            );
            throw new ServiceException(Constants.COMMON_DATA_JSON_INVALID);
        }

        return ApplicationJsonDataResponse.builder()
                .dataJson(commonDataJson)
                .applicationDataJson(applicationDto.getApplicationDataJson())
                .version(commonDataDTO.getVersion())
                .templateTitle(commonDataDTO.getTemplateTitle())
                .charityId(commonDataDTO.getCharityId())
                .applicationNumber(applicationDto.getApplicationNumber())
                .comments(applicationDto.getComments())
                .projectName(applicationDto.getProjectName())
                .funderName(applicationDto.getFunderName())
                .commonDataId(applicationDto.getCommonDataId())
                .status(applicationDto.getStatus())
                .selectedCommonKeys(applicationDto.getSelectedCommonKeys())
                .id(applicationDto.getId())
                .createdBy(applicationDto.getCreatedBy())
                .modifiedBy(applicationDto.getModifiedBy())
                .createdAt(applicationDto.getCreatedAt())
                .modifiedAt(applicationDto.getModifiedAt())
                .build();
    }

    /**
     * Retrieves all applications for a given charity.
     *
     * @param charityId identifier of the charity
     * @return list of applications mapped to {@link ApplicationDTO} 
     */
    @Override
    public List<ApplicationDTO> getApplicationsByCharity(Long charityId) {

        log.info("Fetching applications by charity | charityId={}", charityId);

        List<Application> apps = repository.findByCharityIdAndIsActiveTrueOrderByModifiedAtDesc(charityId);

        log.info("Applications fetched | charityId={} | count={}", charityId, apps.size());

        return apps.stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Creates and persists a new {@link Application} from the provided DTO.
     *
     * @param applicationDTO input application data
     * @return identifier of the newly created application 
     */
    @Override
    public Long createApplication(ApplicationDTO applicationDTO) {

        log.info(
                "Creating application | charityId={} | commonDataId={} | applicationNumber={}",
                applicationDTO.getCharityId(),
                applicationDTO.getCommonDataId(),
                applicationDTO.getApplicationNumber()
        );

        Long id = repository.save(mapToEntity(applicationDTO)).getId();

        log.info(
                "Application created | applicationId={} | charityId={}",
                id,
                applicationDTO.getCharityId()
        );

        return id;
    }

    /**
     * Retrieves all applications in the system.
     *
     * @return list of all applications mapped to {@link ApplicationDTO} 
     */
    @Override
    public List<ApplicationDTO> getAllApplications() {

        log.info("Fetching all applications");

        List<Application> apps = repository.findAll();

        log.info("All applications fetched | count={}", apps.size());

        return apps.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void deleteApplication(Long id) {
        if (!repository.existsByIdAndIsActiveTrue(id)) {
            throw new ServiceException(Constants.APPLICATION_ID_NOT_FOUND);
        }
        repository.updateIsActive(id, false);
    }

    /**
     * Maps an {@link Application} entity to {@link ApplicationDTO}.
     *
     * @param app application entity
     * @return mapped DTO 
     */
    private ApplicationDTO mapToDto(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setApplicationNumber(app.getApplicationNumber());
        dto.setProjectName(app.getProjectName());
        dto.setFunderName(app.getFunderName());
        dto.setCharityId(app.getCharityId());
        dto.setCommonDataId(app.getCommonDataId());
        dto.setStatus(app.getStatus());
        dto.setSelectedCommonKeys(app.getSelectedCommonKeys());
        dto.setApplicationDataJson(app.getApplicationDataJson());
        dto.setComments(app.getComments());
        dto.setCreatedAt(app.getCreatedAt());
        dto.setModifiedAt(app.getModifiedAt());
        dto.setCreatedBy(app.getCreatedBy());
        dto.setModifiedBy(app.getModifiedBy());
        return dto;
    }

    /**
     * Maps an {@link ApplicationDTO} to an {@link Application} entity.
     *
     * @param dto application DTO
     * @return mapped entity 
     */
    private Application mapToEntity(ApplicationDTO dto) {
        Application application = new Application();
        if(dto.getId() != null) {
            application.setId(dto.getId());
        }
        application.setApplicationNumber(dto.getApplicationNumber());
        application.setProjectName(dto.getProjectName());
        application.setFunderName(dto.getFunderName());
        application.setCharityId(dto.getCharityId());
        application.setCommonDataId(dto.getCommonDataId());
        application.setStatus(dto.getStatus());
        application.setSelectedCommonKeys(dto.getSelectedCommonKeys());
        application.setApplicationDataJson(dto.getApplicationDataJson());
        application.setComments(dto.getComments());
        return application;
    }
}
