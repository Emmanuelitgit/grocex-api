package com.grocex_api.productService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "product name cannot be null")
    private String name;
    @NotNull(message = "product quantity cannot be null")
    private int quantity;
    @NotNull(message = "product price cannot be null")
    private Float unitPrice;
    @NotNull(message = "product category id cannot be null")
    private UUID categoryId;
    private Float rating;
    @NotNull(message = "product owner id cannot be null")
    private UUID productOwnerId;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @NotNull(message = "product image cannot be null")
    private byte[] image;
    private UUID createdBy;
    private ZonedDateTime createdAt;
}
