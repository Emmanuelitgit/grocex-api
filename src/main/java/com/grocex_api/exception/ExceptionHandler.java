package com.grocex_api.exception;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnAuthorizeException.class)
    ResponseEntity<ResponseDTO> handleUnAuthorizeException(){
        ResponseDTO response = AppUtils.getResponseDto("Invalid token", HttpStatus.valueOf(401));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<ResponseDTO> handleExpiredJwtException(){
        ResponseDTO response = AppUtils.getResponseDto("Invalid token", HttpStatus.valueOf(401));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }
}
