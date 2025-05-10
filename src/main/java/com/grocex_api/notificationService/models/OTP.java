package com.grocex_api.notificationService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class OTP {
    @Id
    private UUID id;
    private int otpCode;
    private UUID userId;
    private ZonedDateTime expireAt;
    private boolean status;
}
