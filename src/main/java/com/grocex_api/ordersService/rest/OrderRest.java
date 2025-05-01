package com.grocex_api.ordersService.rest;

import com.grocex_api.ordersService.models.Order;
import com.grocex_api.ordersService.serviceImpl.OrderServiceImpl;
import com.grocex_api.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRest {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderRest(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveOrder(@RequestBody Order order){
        return orderService.saveOrder(order);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return orderService.findAll();
    }
}
