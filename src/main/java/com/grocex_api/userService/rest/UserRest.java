package com.grocex_api.userService.rest;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserDTO;
import com.grocex_api.userService.dto.UserPayloadDTO;
import com.grocex_api.userService.dto.UserUpdatePayloadDTO;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserRest {

    private final UserServiceImpl userService;

    @Autowired
    public UserRest(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid UserPayloadDTO user){
        return userService.createUser(user);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> getUserBuId(@PathVariable UUID userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody @Valid UserUpdatePayloadDTO user, @PathVariable UUID userId){
        return userService.updateUser(user, userId);
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO> getUsersAndProducts(){
        return userService.getUsersAndProducts();
    }

}
