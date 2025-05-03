package com.grocex_api.paymentService.rest;

import com.grocex_api.paymentService.dto.PaymentPayload;
import com.grocex_api.paymentService.models.Payment;
import com.grocex_api.paymentService.serviceImpl.PaymentServiceImpl;
import com.grocex_api.response.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentRest {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentRest(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> savePayment(PaymentPayload payment, HttpServletResponse response){
        return paymentService.initializePayment(payment, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return paymentService.findAll();
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDTO> findPaymentById(@PathVariable UUID paymentId){
        return paymentService.findPaymentById(paymentId);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updatePayment(Payment payment){
        return paymentService.updatePayment(payment);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removePayment(UUID paymentId){
        return paymentService.removePayment(paymentId);
    }

    @PostMapping("/webhook")
    public ResponseEntity<HttpStatus> handleWebhook(@RequestBody Map<String, Object> payload) {
        log.info("DATA RECEIVED IN CONTROLLER:{}", payload);

        String event = (String) payload.get("event");

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if ("charge.success".equals(event)) {
            paymentService.processSuccessfulPayment(data);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
