package com.grocex_api.notificationService.rest;

import com.grocex_api.notificationService.dto.OTPPayload;
import com.grocex_api.notificationService.serviceImpl.OTPServiceImpl;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPRest {

    private final OTPServiceImpl otpService;

    @Autowired
    public OTPRest(OTPServiceImpl otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<ResponseDTO> sendOTP(@RequestBody OTPPayload otpPayload){
        otpService.sendOtp(otpPayload);
        ResponseDTO response = AppUtils.getResponseDto("OTP sent", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO> verifyOTP(@RequestBody OTPPayload otpPayload){
        return otpService.verifyOtp(otpPayload);
    }
}
