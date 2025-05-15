package com.grocex_api.paymentService.models;

import com.grocex_api.paymentService.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "payment_tb")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int amount;
    private String transactionId;
    private String reference;
    private String accessCode;
    private String authorizationUrl;
    private String channel;
    private String currency;
    private ZonedDateTime paidAt;
    private UUID orderId;
    private PaymentStatus status;
    private ZonedDateTime createdAt;
}
