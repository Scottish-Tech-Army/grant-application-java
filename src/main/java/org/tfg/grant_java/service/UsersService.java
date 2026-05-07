package org.tfg.grant_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.entity.Users;
import org.tfg.grant_java.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    public List<Users> getUsers(UUID tenantId){
        return userRepository.findByTenantTenantId(tenantId);
    }

}
