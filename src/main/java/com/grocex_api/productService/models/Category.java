package com.grocex_api.productService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "category_tb")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "category name cannot be null")
    private String name;
    private UUID createdBy;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
