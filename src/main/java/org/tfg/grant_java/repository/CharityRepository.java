package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.Charity;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Charity} entities.
 *
 * <p>This repository provides CRUD operations for charities and
 * defines custom query methods to support active-only data access
 * patterns commonly used across the application.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Retrieve charity records by identifier</li>
 *   <li>Filter charities based on active status</li>
 *   <li>Support validation and lookup operations</li>
 * </ul>
 */
public interface CharityRepository extends JpaRepository<Charity, Long> {

    /**
     * Retrieves an active charity by its identifier.
     *
     * @param id the charity identifier
     * @return an {@link Optional} containing the active charity if found
     */
    Optional<Charity> findByIdAndIsActiveTrue(Long id);

    /**
     * Retrieves all active charities.
     *
     * <p>Typically used for dropdowns, validation, or system initialization.</p>
     *
     * @return an {@link Optional} containing the list of active charities
     */
    Optional<List<Charity>> findAllByIsActiveTrue();
}