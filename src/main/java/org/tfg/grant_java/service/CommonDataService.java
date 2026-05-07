package org.tfg.grant_java.service;

import org.tfg.grant_java.dto.CommonDataDTO;

import java.util.List;

/**
 * Service interface defining operations for managing common data templates.
 *
 * <p>This service encapsulates business logic related to reusable
 * common data (JSON-based templates) that can be shared across
 * multiple grant applications within a charity.</p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve common data by identifier</li>
 *   <li>List common data scoped to a specific charity</li>
 *   <li>Bulk-retrieve common data by a list of identifiers</li>
 *   <li>Create or persist new common data templates</li>
 * </ul>
 *
 * <p>Implementations should enforce validation, charity scoping,
 * and error-handling rules.</p>
 */
public interface CommonDataService {

     /**
      * Retrieves common data by its unique identifier.
      *
      * @param id unique identifier of the common data
      * @return common data DTO
      */
     CommonDataDTO getCommonDataById(Long id);

     /**
      * Retrieves all common data associated with a specific charity.
      *
      * @param id identifier of the charity
      * @return list of common data DTOs belonging to the charity
      */
     List<CommonDataDTO> getAllCommonDataByCharityId(Long id);

     /**
      * Retrieves common data records matching the given list of identifiers.
      *
      * <p>Typically used to resolve selected templates in bulk.</p>
      *
      * @param ids list of common data identifiers
      * @return list of matching common data DTOs
      */
     List<CommonDataDTO> getAllCommonDataByListOfIds(List<Long> ids);

     /**
      * Persists a new common data record.
      *
      * <p>Implementations should handle validation, versioning,
      * and default initialization.</p>
      *
      * @param commonDataDTO data required to create the common data record
      * @return identifier of the newly created common data
      */
     Long saveCommonData(CommonDataDTO commonDataDTO);
     void deleteCommonDataById(Long id);
}
