package com.grocex_api.ordersService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderGroupDTO {
    private UUID id;
    private UUID customerId;
    private float totalPrice;
    private String status;
    private int count;
}
