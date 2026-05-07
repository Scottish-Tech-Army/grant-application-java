package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.grant_java.entity.CommonField;

@Repository
public interface CommonFieldRepository extends JpaRepository<CommonField, Long> {}
