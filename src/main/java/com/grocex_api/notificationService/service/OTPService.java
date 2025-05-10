package com.grocex_api.notificationService.service;

import com.grocex_api.notificationService.dto.OTPPayload;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface OTPService {
    public void sendOtp(OTPPayload otpPayload);
    public ResponseEntity<ResponseDTO> verifyOtp(OTPPayload otpPayload);
}
