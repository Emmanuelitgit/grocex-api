package com.grocex_api.userService.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * @description This class is used to map response to the client side.
 * @return
 * @auther Emmanuel Yidana
 * @createdAt 15th  May 2025
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String username;
    private String role;
}