package com.grocex_api.userService.dto;

import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class UserPayloadDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String password;
    private String username;
    private UUID role;
    private UUID createdBy;
    private ZonedDateTime createdAt;

    public UserPayloadDTO() {
    }

    public UserPayloadDTO(ZonedDateTime createdAt, UUID createdBy, String email, String firstName, UUID id, String lastName, String password, int phone, UUID role, String username) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.username = username;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public UUID getRole() {
        return role;
    }

    public void setRole(UUID role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
