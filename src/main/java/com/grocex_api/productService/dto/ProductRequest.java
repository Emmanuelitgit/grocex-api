package com.grocex_api.productService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductRequest {
    private String name;
    private int quantity;
    private int unitPrice;
    private UUID categoryId;
    private UUID productOwnerId;
}