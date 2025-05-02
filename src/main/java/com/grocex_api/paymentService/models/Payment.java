package com.grocex_api.paymentService.models;

import com.grocex_api.productService.dto.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int amount;
    private String transactionId;
    private String reference;
    private String accessCode;
    private String authorizationUrl;
    private UUID orderId;
    private PaymentStatus status;
}
