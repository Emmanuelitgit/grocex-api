package com.grocex_api.paymentService.rest;

import com.grocex_api.paymentService.models.Payment;
import com.grocex_api.paymentService.serviceImpl.PaymentServiceImpl;
import com.grocex_api.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentRest {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentRest(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> savePayment(Payment payment){
        return paymentService.savePayment(payment);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return paymentService.findAll();
    }
}
