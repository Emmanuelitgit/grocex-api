package com.grocex_api.productService.repo;

import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, u.vendor, ct.name As category FROM product p" +
            " JOIN user_tb u ON u.id=p.product_owner_id " +
            "JOIN category_tb ct on p.category_id = ct.id ", nativeQuery = true)
    List<ProductProjection> getProductAndCategory();

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, u.vendor, ct.name As category FROM product p" +
            " JOIN user_tb u ON u.id=p.product_owner_id " +
            "JOIN category_tb ct on p.category_id = ct.id ", nativeQuery = true)
    Page<ProductProjection> getProductAndCategory(Pageable pageable);

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, u.vendor, ct.name As category FROM product p" +
            " JOIN user_tb u ON u.id=p.product_owner_id " +
            "JOIN category_tb ct on p.category_id = ct.id WHERE p.id=? ", nativeQuery = true)
    ProductProjection getProductAndCategoryById(UUID productId);

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, u.vendor, ct.name As category FROM product p" +
            " JOIN user_tb u ON u.id=p.product_owner_id " +
            "JOIN category_tb ct on p.category_id = ct.id WHERE ct.name=? ", nativeQuery = true)
    List<ProductProjection > getProductByCategory(String category);

    @Query(value = "SELECT BIN_TO_UUID(p.id) AS id, p.name AS product, p.unit_price, p.quantity, v.name AS vendor, ct.name As category FROM product p " +
            "JOIN category_tb ct on p.category_id = ct.id " +
            " JOIN vendor_tb v ON v.user_id=p.product_owner_id " +
            " JOIN user_tb u ON u.id=p.product_owner_id WHERE v.name=? ", nativeQuery = true)
    List<ProductProjection > getProductsByVendor(String vendor);
}
