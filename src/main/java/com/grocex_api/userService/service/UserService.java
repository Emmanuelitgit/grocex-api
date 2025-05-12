package com.grocex_api.userService.service;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserDTO;
import com.grocex_api.userService.dto.UserPayloadDTO;
import com.grocex_api.userService.models.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {

    ResponseEntity<ResponseDTO> createUser(UserPayloadDTO userPayloadDTO);
    ResponseEntity<ResponseDTO> getUsers();
    ResponseEntity<ResponseDTO> getUserById(UUID userId);
    ResponseEntity<ResponseDTO> updateUser(UserPayloadDTO user);
    ResponseEntity<ResponseDTO> removeUser(UUID userId);
}
