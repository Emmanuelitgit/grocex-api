package com.grocex_api.ordersService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private String product;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
    private String status;
    private String vendor;
    private LocalDateTime createdAt;
    private String imageUrl;
}
