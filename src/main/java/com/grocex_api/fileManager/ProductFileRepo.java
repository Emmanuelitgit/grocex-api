package com.grocex_api.fileManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductFileRepo extends JpaRepository<ProductFile, UUID> {
}
