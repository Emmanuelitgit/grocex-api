package com.grocex_api.notificationService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationPayload {
    private String fullName;
    private List<Object> products;
    private String message;
    private String email;
    private float orderTotals;

    @Data
    static class Product{
        private String product;
        private int price;
    }
}
