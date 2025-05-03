package com.grocex_api.ordersService.repo;

import com.grocex_api.ordersService.dto.OrderProjection;
import com.grocex_api.ordersService.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT BIN_TO_UUID(o.id) AS orderId, p.name AS product, o.total_price, o.quantity, o.unit_price, CONCAT(u.first_name, ' ', u.last_name) AS customer, u.username, u.email FROM order_tb o " +
            "JOIN product p ON o.product_id=p.id " +
            "JOIN user_tb u ON u.id=o.customer_id ", nativeQuery = true)
    List<OrderProjection> getOrderDetails();
}
