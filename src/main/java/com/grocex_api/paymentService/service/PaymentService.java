package com.grocex_api.paymentService.service;

import com.grocex_api.paymentService.dto.PaymentPayload;
import com.grocex_api.paymentService.models.Payment;
import com.grocex_api.productService.models.Product;
import com.grocex_api.response.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    ResponseEntity<ResponseDTO> initializePayment(PaymentPayload payment, HttpServletResponse response);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findPaymentById(UUID paymentId);
    ResponseEntity<ResponseDTO> updatePayment(Payment payment);
    ResponseEntity<ResponseDTO> removePayment(UUID paymentId);
    void processSuccessfulPayment(Map<String, Object> data);

}
