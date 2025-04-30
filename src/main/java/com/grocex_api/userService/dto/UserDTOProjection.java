package com.grocex_api.userService.dto;

import java.util.UUID;

public interface UserDTOProjection {
    UUID getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getPhone();
    String getUsername();
    String getRole();
}
