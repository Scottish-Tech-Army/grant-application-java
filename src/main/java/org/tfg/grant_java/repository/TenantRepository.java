package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.grant_java.entity.Tenants;

import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenants, UUID> {
}
