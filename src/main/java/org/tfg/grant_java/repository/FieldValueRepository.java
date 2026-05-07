package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.grant_java.entity.FieldValue;

@Repository
public interface FieldValueRepository extends JpaRepository<FieldValue, Long> {}
