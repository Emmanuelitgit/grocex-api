package com.grocex_api.ordersService.service;

import com.grocex_api.ordersService.models.Order;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface OrderService {
    ResponseEntity<ResponseDTO> saveOrder(Order order);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findOrderById(UUID orderId);
    ResponseEntity<ResponseDTO> updateOrder(Order order);
    ResponseEntity<ResponseDTO> removeOrder(UUID orderId);
}
