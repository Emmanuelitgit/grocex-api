package com.grocex_api.productService.repo;

import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, ct.name As category FROM product p " +
            "JOIN category_tb ct on p.category_id = ct.id ", nativeQuery = true)
    List<ProductProjection> getProductAndCategory();

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, ct.name As category FROM product p " +
            "JOIN category_tb ct on p.category_id = ct.id WHERE p.id=? ", nativeQuery = true)
    ProductProjection getProductAndCategoryById(UUID productId);
}
