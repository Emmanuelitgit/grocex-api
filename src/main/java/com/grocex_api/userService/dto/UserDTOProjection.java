package com.grocex_api.userService.dto;

import java.util.UUID;

/**
 * @description This interface class is used to map to the sql query to return user details.
 * @return
 * @auther Emmanuel Yidana
 * @createdAt 15th  May 2025
 */
public interface UserDTOProjection {
    UUID getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPhone();
    String getUsername();
    String getVendor();
    String getRole();
}
