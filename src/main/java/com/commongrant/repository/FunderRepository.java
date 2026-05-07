package com.commongrant.repository;

import com.commongrant.model.Funder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunderRepository extends JpaRepository<Funder, Long> {
    List<Funder> findByActiveTrue();

    @Query("SELECT f FROM Funder f WHERE f.active = true " +
           "AND (:focus IS NULL OR f.focusAreas LIKE %:focus%) " +
           "AND (:region IS NULL OR f.geographicFocus = :region OR f.geographicFocus = 'Global')")
    Page<Funder> search(@Param("focus") String focus,
                        @Param("region") String region,
                        Pageable pageable);
}

