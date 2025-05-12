package com.grocex_api.userService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
@Data
public class UserPayloadDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String password;
    private String username;
    private UUID role;
    private String vendor;
    private UUID createdBy;
    private ZonedDateTime createdAt;
}
