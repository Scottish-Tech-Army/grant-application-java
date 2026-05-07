package org.tfg.grant_java.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.grant_java.entity.Applications;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Applications, UUID> {
    Page<List<Applications>> findByTenant_TenantId(UUID tenantId, Pageable pageable);

    Page<List<Applications>> findByTenant_TenantIdAndStatusNot(UUID tenantId, String status, Pageable pageable);

    @Query(
            value = """
                        SELECT status,
                               COUNT(*) AS count
                        FROM applications
                        WHERE tenant_id = :tenantId
                        GROUP BY status
                    """,
            nativeQuery = true
    )
    List<Object[]> countByStatusAndTenant(
            @Param("tenantId") UUID tenantId
    );


    List<Applications> findTop5ByOrderByCreatedAtDesc();
}
