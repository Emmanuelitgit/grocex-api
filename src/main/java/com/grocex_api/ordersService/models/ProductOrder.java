package com.grocex_api.ordersService.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "product_order_tb")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID productId;
    private UUID orderId;
    private int unitPrice;
    private int totalPrice;
    private int quantity;
}
