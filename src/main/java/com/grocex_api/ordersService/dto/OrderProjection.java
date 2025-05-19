package com.grocex_api.ordersService.dto;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProjection{
    UUID getUserId();
    String getCustomer();
    String getUsername();
    String getEmail();
    UUID getOrderId();
    String getProduct();
    Integer getQuantity();
    Integer getUnitPrice();
    Integer getTotalPrice();
    String getStatus();
    Integer getTotals();
}
