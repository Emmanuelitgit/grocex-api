package com.grocex_api.userService.dto;

import java.util.UUID;

/**
 * @description This interface class is used to map to the sql query to return user and product details.
 * @return
 * @auther Emmanuel Yidana
 * @createdAt 15th  May 2025
 */
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
