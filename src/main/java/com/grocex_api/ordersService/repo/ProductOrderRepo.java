package com.grocex_api.ordersService.repo;

import com.grocex_api.ordersService.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductOrderRepo extends JpaRepository<ProductOrder, UUID> {
    ProductOrder findByOrderId(UUID orderId);
}
