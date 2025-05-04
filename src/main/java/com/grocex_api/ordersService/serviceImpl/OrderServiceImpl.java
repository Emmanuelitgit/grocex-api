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
import com.grocex_api.userService.models.User;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * @param orders
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> saveOrder(List<Order> orders) {
        try{
            log.info("In place order method:->>>>>>>>");
           if (orders.isEmpty()){
               log.error("order payload cannot be be null{}", HttpStatus.BAD_REQUEST);
               ResponseDTO  response = AppUtils.getResponseDto("order payload cannot be null", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }
           List<Order>res = new ArrayList<>();
           for (Order order: orders){
               Optional<Product> productOptional = productRepo.findById(order.getProductId());
               if (productOptional.isEmpty()){
                   log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                   ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                   return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
               }
               // checking if the order quantity exceed the available quantity of product
               Product product = productOptional.get();
               if (order.getQuantity()>product.getQuantity()){
                   log.error("\"order quantity exceeds product availability for:->>\" + \" \" + product.getName(){}", HttpStatus.BAD_REQUEST);
                   ResponseDTO  response = AppUtils.getResponseDto("order quantity exceeds product availability for:->>" + " " + product.getName(), HttpStatus.BAD_REQUEST);
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
               order.setStatus(OrderStatus.PENDING.toString());
               Order orderRes = orderRepo.save(order);
               res.add(orderRes);
               productRepo.save(product);
           }

            ResponseDTO  response = AppUtils.getResponseDto("product ordered successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * @description This method is used to fetch all orders from the db.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
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
            Map<Object, List<Object>> ordersObj = new HashMap<>();
            List<Object> res = new ArrayList<>();
            for (OrderProjection order:orders){
                Map<String, Object> data = new HashMap<>();
                data.put("userId", order.getUserId());
                data.put("full name", order.getCustomer());
                data.put("username", order.getUsername());
                data.put("email", order.getEmail());

                if (!ordersObj.containsKey(order.getCustomer())){
                    ordersObj.put(order.getCustomer(), new ArrayList<>());
                }

                Map<String, Object> orderItems  = new HashMap<>();
                orderItems.put("orderId", order.getOrderId());
                orderItems.put("product", order.getProduct());
                orderItems.put("unitPrice", order.getUnitPrice());
                orderItems.put("quantity", order.getQuantity());
                orderItems.put("totalPrice", order.getTotalPrice());
                orderItems.put("status", order.getStatus());

                ordersObj.get(order.getCustomer()).add(orderItems);

                data.put("orders", ordersObj.get(order.getCustomer()));
                res.add(data);

            }
            // removing duplicates
            Set<Object> setResponse = new HashSet<>(res);
            ResponseDTO  response = AppUtils.getResponseDto("orders records fetched successfully", HttpStatus.OK, setResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findOrderById(UUID orderId) {
        try{
            log.info("In get order method:->>>>>>");
            OrderProjection order = orderRepo.getOrderDetailsById(orderId);
            if (order == null){
                log.error("no order record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no order record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Map<Object, List<Object>> ordersObj = new HashMap<>();
            List<Object> res = new ArrayList<>();
            Map<String, Object> data = new HashMap<>();
            data.put("userId", order.getUserId());
            data.put("full name", order.getCustomer());
            data.put("username", order.getUsername());
            data.put("email", order.getEmail());

            if (!ordersObj.containsKey(order.getCustomer())){
                ordersObj.put(order.getCustomer(), new ArrayList<>());
            }

            Map<String, Object> orderItems  = new HashMap<>();
            orderItems.put("orderId", order.getOrderId());
            orderItems.put("product", order.getProduct());
            orderItems.put("unitPrice", order.getUnitPrice());
            orderItems.put("quantity", order.getQuantity());
            orderItems.put("totalPrice", order.getTotalPrice());

            ordersObj.get(order.getCustomer()).add(orderItems);

            data.put("orders", ordersObj.get(order.getCustomer()));
            res.add(data);
            // removing duplicates
            Set<Object> setResponse = new HashSet<>(res);
            ResponseDTO  response = AppUtils.getResponseDto("order records fetched successfully", HttpStatus.OK, setResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findOrderByUserId(UUID userId) {
        try{
            log.info("In get order method:->>>>>>");
            OrderProjection order = orderRepo.getOrderDetailsByUserId(userId);
            if (order == null){
                log.error("no order record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no order record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Map<Object, List<Object>> ordersObj = new HashMap<>();
            List<Object> res = new ArrayList<>();
            Map<String, Object> data = new HashMap<>();
            data.put("userId", order.getUserId());
            data.put("full name", order.getCustomer());
            data.put("username", order.getUsername());
            data.put("email", order.getEmail());

            if (!ordersObj.containsKey(order.getCustomer())){
                ordersObj.put(order.getCustomer(), new ArrayList<>());
            }

            Map<String, Object> orderItems  = new HashMap<>();
            orderItems.put("orderId", order.getOrderId());
            orderItems.put("product", order.getProduct());
            orderItems.put("unitPrice", order.getUnitPrice());
            orderItems.put("quantity", order.getQuantity());
            orderItems.put("totalPrice", order.getTotalPrice());

            ordersObj.get(order.getCustomer()).add(orderItems);

            data.put("orders", ordersObj.get(order.getCustomer()));
            res.add(data);
            // removing duplicates
            Set<Object> setResponse = new HashSet<>(res);
            ResponseDTO  response = AppUtils.getResponseDto("order records fetched successfully", HttpStatus.OK, setResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> updateOrder(Order order) {
       try{
           log.info("In update order method:->>>>>>");
           Optional<Order> orderOptional = orderRepo.findById(order.getId());
           if (orderOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }

           Optional<Product> productOptional = productRepo.findById(order.getProductId());
           if (productOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           Product product = productOptional.get();

           // calculating quantities and total price.
           int totalPrice = order.getUnitPrice()*order.getQuantity();
           int previousQuantity = orderOptional.get().getQuantity() + product.getQuantity();
           int remainingQuantity = previousQuantity-order.getQuantity();
           product.setQuantity(remainingQuantity);
           productRepo.save(product);

           // updating the existing order record with the updated info.
           Order existingOrderData = orderOptional.get();
           existingOrderData.setQuantity(order.getQuantity());
           existingOrderData.setTotalPrice(totalPrice);
           orderRepo.save(existingOrderData);

           log.info("order updated successfully:->>>>>>");
           ResponseDTO  response = AppUtils.getResponseDto("order record updated successfully", HttpStatus.OK);
           return new ResponseEntity<>(response, HttpStatus.OK);
       }catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<ResponseDTO> removeOrder(UUID orderId) {
        try {
            log.info("In remove order method:->>>>>>");
            Optional<Order> orderOptional = orderRepo.findById(orderId);
            if (orderOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            orderRepo.deleteById(orderId);
            log.info("order removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("order record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
