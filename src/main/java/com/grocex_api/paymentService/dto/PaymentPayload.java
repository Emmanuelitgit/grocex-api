package com.grocex_api.paymentService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
public class PaymentPayload {
    private int amount;
    private String email;
    private UUID orderId;
}
