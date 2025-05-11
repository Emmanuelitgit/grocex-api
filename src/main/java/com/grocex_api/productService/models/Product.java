package com.grocex_api.productService.models;

import jakarta.persistence.*;
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
    private String name;
    private int quantity;
    private int unitPrice;
    private UUID categoryId;
    private UUID fileId;
    private Integer rating;
    private UUID productOwnerId;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;
    private UUID createdBy;
    private ZonedDateTime createdAt;
}
