package com.grocex_api.productService.dto;

import java.util.UUID;

public interface ProductProjection {
    UUID getId();
    String getProduct();
    Integer getUnitPrice();
    Integer getQuantity();
    String getCategory();
    String getVendor();
}
