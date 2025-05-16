package com.grocex_api.ordersService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
public class OrderPayload {
    @NotNull(message = "quantity cannot be null")
    private int quantity;
    @NotNull(message = "customer id cannot be null")
    private UUID customerId;
    @NotNull(message = "product id cannot be null")
    private UUID productId;
    // use for updating payload sake
    private UUID orderId;
}
