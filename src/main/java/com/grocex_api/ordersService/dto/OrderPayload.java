package com.grocex_api.ordersService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
public class OrderPayload {
    private int quantity;
    private UUID customerId;
    private UUID productId;
    // for updating sake
    private UUID orderId;
}
