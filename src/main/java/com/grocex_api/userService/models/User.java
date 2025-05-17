package com.grocex_api.userService.models;

import com.grocex_api.config.AuditorData;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_tb")
public class User extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String username;
    private String password;
}
