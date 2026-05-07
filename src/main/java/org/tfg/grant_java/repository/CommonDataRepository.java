package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.entity.CommonData;

import java.util.List;

public interface CommonDataRepository
        extends JpaRepository<CommonData, Long> {
    List<CommonData> findByCharityIdAndIsActiveTrue(Long charityId);

    List<CommonData> findByIdIn(List<Long> ids);

    boolean existsByIdAndIsActiveTrue(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE CommonData a SET a.isActive = :active WHERE a.id = :id")
    int updateIsActive(@Param("id") Long id, @Param("active") Boolean active);
}