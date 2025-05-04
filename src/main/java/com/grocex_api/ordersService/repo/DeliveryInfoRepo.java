package com.grocex_api.ordersService.repo;

import com.grocex_api.ordersService.models.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryInfoRepo extends JpaRepository<DeliveryInfo, UUID> {
}
