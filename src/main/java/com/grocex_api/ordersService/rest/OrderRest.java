package com.grocex_api.ordersService.rest;

import com.grocex_api.ordersService.dto.OrderPayload;
import com.grocex_api.ordersService.models.Order;
import com.grocex_api.ordersService.serviceImpl.OrderServiceImpl;
import com.grocex_api.response.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderRest {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderRest(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveOrder(@RequestBody List<OrderPayload> orders){
        return orderService.saveOrder(orders);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        log.info("Authentication:->>>{}", SecurityContextHolder.getContext().getAuthentication());
        return orderService.findAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> findOrderById(@PathVariable UUID orderId){
        return orderService.findOrderById(orderId);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO> findOrderByUserId(@PathVariable UUID userId){
        return orderService.findOrderByUserId(userId);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateOrder(OrderPayload order){
        return orderService.updateOrder(order);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removeOrder(UUID orderId){
        return orderService.removeOrder(orderId);
    }
}
