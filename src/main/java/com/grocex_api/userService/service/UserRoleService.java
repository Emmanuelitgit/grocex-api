package com.grocex_api.userService.service;

import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserRoleService {
    ResponseEntity<ResponseDTO> saveUserRole(UUID userId, UUID roleId);
}
