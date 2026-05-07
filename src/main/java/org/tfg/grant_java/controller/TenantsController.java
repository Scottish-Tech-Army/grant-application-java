package org.tfg.grant_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.grant_java.dto.TenantDTO;
import org.tfg.grant_java.entity.Tenants;
import org.tfg.grant_java.service.TenantsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/tenants")
public class TenantsController {
    @Autowired
    private TenantsService tenantsService;
    @GetMapping()
    public ResponseEntity<List<TenantDTO>> getAllTenants(){
        List<Tenants> tenants=tenantsService.getTenants();
        List<TenantDTO> tenantDTOList = new ArrayList<>();
        for (Tenants tenant: tenants) {
            TenantDTO tenantDTO = new TenantDTO();
            tenantDTO.setName(tenant.getName());
            tenantDTO.setStatus(tenant.getStatus());
            tenantDTO.setCreatedAt(tenant.getCreatedAt());
            tenantDTO.setTenantId(tenant.getTenantId());
            tenantDTOList.add(tenantDTO);
        }
        return ResponseEntity.ok(tenantDTOList);
    }
}
