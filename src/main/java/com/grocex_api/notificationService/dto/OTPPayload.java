package com.grocex_api.notificationService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class OTPPayload {
    @NotNull(message = "email cannot be null")
    private String email;
    @NotNull(message = "otp code cannot be null")
    private int otpCode;
}
