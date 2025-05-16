package com.grocex_api.productService.models;

import com.grocex_api.config.AuditorData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "category_tb")
public class Category extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "category name cannot be null")
    private String name;
    private UUID createdBy;
}
