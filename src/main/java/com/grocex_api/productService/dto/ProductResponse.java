package com.grocex_api.productService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String product;
    private Integer unitPrice;
    private Integer quantity;
    private String category;
    private String vendor;
    private String imageUrl;
}
