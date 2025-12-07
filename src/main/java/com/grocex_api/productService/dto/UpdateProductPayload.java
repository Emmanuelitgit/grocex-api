package com.grocex_api.productService.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class UpdateProductPayload {
    @NotNull(message = "Product id cannot be null")
    private UUID id;
    private String name;
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    @Min(value = 10, message = "product quantity cannot be less than 10")
    private Integer quantity;
    private Float price;
    private UUID categoryId;
    private Float rating;
    private MultipartFile file;
}
