package org.tfg.grant_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.entity.Tenants;
import org.tfg.grant_java.repository.TenantRepository;

import java.util.List;

@Service
public class TenantsService {
    @Autowired
    private TenantRepository tenantRepository;

    public List<Tenants> getTenants(){
        return tenantRepository.findAll();
    }
}
