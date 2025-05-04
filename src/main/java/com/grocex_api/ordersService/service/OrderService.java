package com.grocex_api.ordersService.service;

import com.grocex_api.ordersService.dto.OrderPayload;
import com.grocex_api.ordersService.models.Order;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ResponseEntity<ResponseDTO> saveOrder(List<OrderPayload> orders);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findOrderById(UUID orderId);
    ResponseEntity<ResponseDTO> findOrderByUserId(UUID userId);
    ResponseEntity<ResponseDTO> updateOrder(OrderPayload order);
    ResponseEntity<ResponseDTO> removeOrder(UUID orderId);
}
