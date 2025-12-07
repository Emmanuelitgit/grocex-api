package com.grocex_api.productService.repo;

import com.grocex_api.productService.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, UUID> {
    Optional<Vendor> findByName(String name);

    Optional<Vendor> findByUserId(UUID userId);
}
