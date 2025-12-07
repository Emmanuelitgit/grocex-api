package com.grocex_api.productService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private UUID id;
    @NotNull(message = "product name cannot be null")
    private String name;
    @NotNull(message = "product quantity cannot be null")
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    @Min(value = 10, message = "product quantity cannot be less than 10")
    private Integer quantity;
    @NotNull(message = "product price cannot be null")
    private Float price;
    @NotNull(message = "product category id cannot be null")
    private UUID categoryId;
    @NotNull(message = "product owner id cannot be null")
    private UUID ownerId;
//    @NotNull(message = "product image cannot be null")
    private MultipartFile file;
}
