package com.grocex_api.userService.dto;

import com.grocex_api.productService.dto.VendorStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
@Data
public class UserPayloadDTO {
    private UUID id;
    @NotNull(message = "first name cannot be null")
    @Size(message = "first name is too long")
    private String firstName;
    @NotNull(message = "last name cannot be null")
    @Size(message = "last name is too long")
    private String lastName;
    @NotNull(message = "email cannot be null")
    @Email(message = "invalid email")
    private String email;
    @NotNull(message = "phone number cannot be null")
    @Size(max = 10, min = 10, message = "Phone number must be between 10 and 10")
    private String phone;
    @NotNull(message = "password cannot be null")
//    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).*$",
//            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String password;
    @NotNull(message = "username cannot be null")
    private String username;
    @NotNull(message = "role id cannot be null")
    private UUID role;
//    @NotNull(message = "vendor name cannot be null")
    private String vendor;
}
