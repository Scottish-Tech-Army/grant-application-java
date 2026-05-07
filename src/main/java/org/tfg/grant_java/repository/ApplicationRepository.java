package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.entity.Application;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Application} entities.
 *
 * <p>This repository provides standard CRUD operations via
 * {@link JpaRepository} and defines additional query methods
 * specific to the {@code Application} domain.</p>
 *
 * <p><b>Key capabilities:</b></p>
 * <ul>
 *   <li>Persist and retrieve application records</li>
 *   <li>Query applications by associated charity</li>
 * </ul>
 */
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCharityIdAndIsActiveTrueOrderByModifiedAtDesc(Long charityId);

    Optional<Application> findByIdAndIsActiveTrue(Long charityId);

    boolean existsByIdAndIsActiveTrue(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Application a SET a.isActive = :active WHERE a.id = :id")
    int updateIsActive(@Param("id") Long id, @Param("active") Boolean active);

}

