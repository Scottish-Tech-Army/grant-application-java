package org.tfg.grant_java.controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.TenantDTO;
import org.tfg.grant_java.dto.UserDTO;
import org.tfg.grant_java.entity.Tenants;
import org.tfg.grant_java.entity.Users;
import org.tfg.grant_java.service.UsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/v1/users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @GetMapping(value = "/{tenantId}")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PathVariable UUID tenantId){
        List<Users> users = usersService.getUsers(tenantId);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(user.getName());
            userDTO.setUserId(user.getUserId());
            userDTO.setEmail(user.getEmail());
            userDTO.setCreatedAt(user.getCreatedAt());
            userDTOList.add(userDTO);
        }
        return ResponseEntity.ok(userDTOList);
    }
}
