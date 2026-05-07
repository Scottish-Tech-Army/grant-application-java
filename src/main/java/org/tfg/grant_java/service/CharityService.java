package org.tfg.grant_java.service;

import org.tfg.grant_java.dto.CharityDTO;

import java.util.List;

/**
 * Service interface defining charity-related operations.
 *
 * <p>This service encapsulates business logic for retrieving charity
 * information and acts as the abstraction layer between controllers
 * and persistence for charity data.</p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve active charities available in the system</li>
 *   <li>Apply business rules related to charity visibility and status</li>
 * </ul>
 *
 * <p>Implementations should ensure that only active charities are exposed
 * and handle any required validation or error scenarios.</p>
 */
public interface CharityService {

     /**
      * Retrieves all active charities.
      *
      * <p>Typically used for login flows, dropdown selections,
      * or validation of charity context.</p>
      *
      * @return list of active charity data transfer objects
      */
     List<CharityDTO> getAllActiveCharities();
}