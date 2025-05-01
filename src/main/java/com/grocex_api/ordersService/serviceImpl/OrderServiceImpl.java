package com.grocex_api.ordersService.serviceImpl;

import com.grocex_api.ordersService.dto.OrderProjection;
import com.grocex_api.ordersService.models.Order;
import com.grocex_api.ordersService.repo.OrderRepo;
import com.grocex_api.ordersService.service.OrderService;
import com.grocex_api.productService.dto.OrderStatus;
import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.repo.ProductRepo;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    /**
     * @description This method is used to save order to the db given the required parameters.
     * @param order
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> saveOrder(Order order) {
        try{
            log.info("In place order method:->>>>>>>>");
            Optional<Product> productOptional = productRepo.findById(order.getProductId());
            if (productOptional.isEmpty()){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // checking if the order quantity exceed the available quantity of product
            Product product = productOptional.get();
            if (order.getQuantity()>product.getQuantity()){
                ResponseDTO  response = AppUtils.getResponseDto("order quantity exceeds product availability", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // deducting the order quantity from the product quantity to get the remaining quantity.
            int quantityRemaining = product.getQuantity()- order.getQuantity();
            product.setQuantity(quantityRemaining);

            // calculating the order total price
            int orderTotalPrice = order.getQuantity()*product.getUnitPrice();

            // setting the order updated details
            order.setTotalPrice(orderTotalPrice);
            order.setUnitPrice(product.getUnitPrice());
            order.setStatus(OrderStatus.PENDING);
            Order orderRes = orderRepo.save(order);
            productRepo.save(product);

            ResponseDTO  response = AppUtils.getResponseDto("product ordered successfully", HttpStatus.CREATED, orderRes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In get all orders method:->>>>>>");
            List<OrderProjection> orders = orderRepo.getOrderDetails();
            if (orders.isEmpty()){
                log.error("no order record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no order record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("products records fetched successfully", HttpStatus.OK, orders);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findOrderById(UUID orderId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateOrder(Order order) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeOrder(UUID orderId) {
        return null;
    }
}
