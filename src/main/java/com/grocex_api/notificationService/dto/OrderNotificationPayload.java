package com.grocex_api.notificationService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
public class OrderNotificationPayload {
    private String fullName;
    private List<Product> products;
    private String message;

    @Data
    static class Product{
        private String product;
        private int orderAmount;
    }
}
