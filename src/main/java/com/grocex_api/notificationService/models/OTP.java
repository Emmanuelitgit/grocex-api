package com.grocex_api.notificationService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int otpCode;
    private UUID userId;
    private ZonedDateTime expireAt;
    private boolean status;
}
