package com.grocex_api.ordersService.dto;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProjection{
    UUID getOrderId();
    String getProduct();
    Integer getQuantity();
    Integer getUnitPrice();
    Integer getTotalPrice();
    String getCustomer();
}
