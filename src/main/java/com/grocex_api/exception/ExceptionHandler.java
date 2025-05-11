package com.grocex_api.exception;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException){
        Map<String, Object> res = new HashMap<>();
        res.put("message", notFoundException.getMessage());
        res.put("status", HttpStatus.valueOf(404));
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(404));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerException.class)
    ResponseEntity<Object> handleServerException(ServerException serverException){
        Map<String, Object> res = new HashMap<>();
        res.put("message", serverException.getMessage());
        res.put("status", HttpStatus.valueOf(500));
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(500));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    ResponseEntity<Object> handleBadRequestException(BadRequestException badRequestException){
        Map<String, Object> res = new HashMap<>();
        res.put("message", badRequestException.getMessage());
        res.put("status", HttpStatus.valueOf(400));
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(400));
    }
}
