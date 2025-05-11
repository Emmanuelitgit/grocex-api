package com.grocex_api.notificationService.repo;

import com.grocex_api.notificationService.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OTPRepo extends JpaRepository<OTP, UUID> {
    OTP findByUserId(UUID userId);
}
