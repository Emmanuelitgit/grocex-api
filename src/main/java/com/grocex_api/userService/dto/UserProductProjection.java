package com.grocex_api.userService.dto;

import java.util.UUID;

public interface UserProductProjection {
    UUID getUserId();
    UUID getProductId();
    String getFullName();
    String getVendor();
    String getProduct();
    Integer getUnitPrice();
    Integer getQuantity();
    String getCategory();
    String getEmail();
}
