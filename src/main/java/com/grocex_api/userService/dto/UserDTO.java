package com.grocex_api.userService.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Setter
@Getter
@Component
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String username;
    private String role;

    public UserDTO() {
    }

    public UserDTO(UUID id,String firstName,String lastName, String email, int phone, String username, String role) {
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.role = role;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}