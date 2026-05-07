package org.tfg.grant_java.service;


import org.tfg.grant_java.dto.ApplicationDTO;
import org.tfg.grant_java.model.ApplicationJsonDataResponse;
import org.tfg.grant_java.model.ApplicationResponse;

import java.util.List;

/**
 * Service interface defining application-related business operations.
 *
 * <p>This service acts as the primary abstraction for managing
 * grant applications and encapsulates business logic related to
 * application creation, retrieval, and listing.</p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve a single application with full JSON and template details</li>
 *   <li>List applications scoped to a specific charity</li>
 *   <li>Create new grant applications</li>
 *   <li>Retrieve all applications for administrative use cases</li>
 * </ul>
 *
 * <p>Implementations of this interface should enforce validation,
 * authorization, and error-handling rules.</p>
 */
public interface ApplicationService {

    /**
     * Retrieves a single application by its identifier.
     *
     * <p>The response includes application metadata, selected common keys,
     * and associated JSON payloads.</p>
     *
     * @param id unique identifier of the application
     * @return aggregated application and common data response
     */
    ApplicationJsonDataResponse getApplication(Long id);

    /**
     * Retrieves all applications associated with the given charity.
     *
     * @param charityId identifier of the charity
     * @return list of application DTOs for the specified charity
     */
    List<ApplicationDTO> getApplicationsByCharity(Long charityId);

    /**
     * Creates a new grant application.
     *
     * <p>Implementations should persist application metadata, associate
     * common data selections, and return the newly created application ID.</p>
     *
     * @param applicationDTO data required to create the application
     * @return identifier of the newly created application
     */
    Long createApplication(ApplicationDTO applicationDTO);

    /**
     * Retrieves all applications in the system.
     *
     * <p>Typically used for administrative or reporting purposes.</p>
     *
     * @return list of all application DTOs
     */
    List<ApplicationDTO> getAllApplications();

    void deleteApplication(Long id);
}

