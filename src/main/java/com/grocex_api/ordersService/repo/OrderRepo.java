package com.grocex_api.ordersService.repo;

import com.grocex_api.ordersService.dto.ItemsCountProjection;
import com.grocex_api.ordersService.dto.OrderProjection;
import com.grocex_api.ordersService.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT BIN_TO_UUID(o.id) AS orderId, o.status, p.name AS product, po.unit_price, po.quantity, po.total_price, o.total_price AS totals, BIN_TO_UUID(u.id) AS userId, CONCAT(u.first_name, ' ', u.last_name) AS customer, u.username, u.email FROM product_order_tb po " +
            "JOIN product p ON po.product_id=p.id " +
            "JOIN order_tb o ON o.id=po.order_id " +
            "JOIN user_tb u ON u.id=o.customer_id ", nativeQuery = true)
    List<OrderProjection> getOrderDetails();

    @Query(value = "SELECT BIN_TO_UUID(o.id) AS orderId, o.status, p.name AS product, po.unit_price, po.quantity, po.total_price, o.total_price AS totals, BIN_TO_UUID(u.id) AS userId, CONCAT(u.first_name, ' ', u.last_name) AS customer, u.username, u.email FROM product_order_tb po " +
            "JOIN product p ON po.product_id=p.id " +
            "JOIN order_tb o ON o.id=po.order_id " +
            "JOIN user_tb u ON u.id=o.customer_id WHERE o.id=? ", nativeQuery = true)
    List<OrderProjection> getOrderDetailsById(UUID orderId);

    @Query(value = "SELECT BIN_TO_UUID(o.id) AS orderId, o.status, p.name AS product, po.unit_price, po.quantity, po.total_price, o.total_price AS totals, BIN_TO_UUID(u.id) AS userId, CONCAT(u.first_name, ' ', u.last_name) AS customer, u.username, u.email FROM product_order_tb po " +
            "JOIN product p ON po.product_id=p.id " +
            "JOIN order_tb o ON o.id=po.order_id " +
            "JOIN user_tb u ON u.id=o.customer_id WHERE u.id=? ", nativeQuery = true)
    List<OrderProjection> getOrderDetailsByUserId(UUID userId);

    @Query(value = "SELECT COUNT(id) AS itemsCount FROM product_order_tb p " +
            "WHERE  p.order_id=? GROUP BY p.order_id ", nativeQuery = true)
    ItemsCountProjection getItemsCountByOderId(UUID orderId);

    List<Order> findByCustomerId(UUID customerId);


}
