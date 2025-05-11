package com.grocex_api.notificationService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class OTPPayload {
    private String email;
    private int otpCode;
}
