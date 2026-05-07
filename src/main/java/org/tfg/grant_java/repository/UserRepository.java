package org.tfg.grant_java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}
