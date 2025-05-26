package com.grocex_api.ordersService.models;

import com.grocex_api.config.AuditorData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "product_order_tb")
public class ProductOrder extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID productId;
    private UUID orderId;
    private float unitPrice;
    private float totalPrice;
    private int quantity;
}
