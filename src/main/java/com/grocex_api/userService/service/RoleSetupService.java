package com.grocex_api.userService.service;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.RoleSetup;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoleSetupService {

    ResponseEntity<ResponseDTO> saveRole(RoleSetup roleSetup);
    ResponseEntity<ResponseDTO> updateRole(RoleSetup roleSetup, UUID roleId);
    ResponseEntity<ResponseDTO> findRoleById(UUID roleId);
}
