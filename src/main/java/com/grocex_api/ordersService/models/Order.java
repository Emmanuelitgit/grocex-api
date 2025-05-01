package com.grocex_api.ordersService.models;

import com.grocex_api.productService.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "order_tb")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID productId;
    private UUID customerId;
    private int unitPrice;
    private int totalPrice;
    private int quantity;
    private OrderStatus status;
}
