package com.grocex_api.userService.dto;

import com.grocex_api.productService.dto.VendorStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdatePayloadDTO {
    @Size(message = "First name is too long")
    private String firstName;
    @Size(message = "Last name is too long")
    private String lastName;
    @Email(message = "Invalid email")
    private String email;
    @Size(max = 10, min = 10, message = "Phone number must be between 10 and 10")
    private String phone;
    private String username;
    private UUID role;
    private String vendorStatus;
    private String vendor;
}
