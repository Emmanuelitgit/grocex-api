package com.grocex_api.ordersService.serviceImpl;

import com.grocex_api.exception.NotFoundException;
import com.grocex_api.notificationService.dto.OrderNotificationPayload;
import com.grocex_api.notificationService.serviceImpl.OrderNotificationServiceImpl;
import com.grocex_api.ordersService.dto.OrderPayload;
import com.grocex_api.ordersService.dto.OrderProjection;
import com.grocex_api.ordersService.models.Order;
import com.grocex_api.ordersService.models.ProductOrder;
import com.grocex_api.ordersService.repo.OrderRepo;
import com.grocex_api.ordersService.repo.ProductOrderRepo;
import com.grocex_api.ordersService.service.OrderService;
import com.grocex_api.productService.dto.OrderStatus;
import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.repo.ProductRepo;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.UserRepo;
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
    private final ProductOrderRepo productOrderRepo;
    private final OrderNotificationServiceImpl orderNotificationService;
    private final UserRepo userRepo;

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo, ProductRepo productRepo, ProductOrderRepo productOrderRepo, OrderNotificationServiceImpl orderNotificationService, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.productOrderRepo = productOrderRepo;
        this.orderNotificationService = orderNotificationService;
        this.userRepo = userRepo;
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
    public ResponseEntity<ResponseDTO> saveOrder(List<OrderPayload> orders) {
        try{
            log.info("In place order method:->>>>>>>>");
           if (orders.isEmpty()){
               log.error("order payload cannot be be null{}", HttpStatus.BAD_REQUEST);
               ResponseDTO  response = AppUtils.getResponseDto("order payload cannot be null", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }

           // grouping all order details as one order.
            float entireOrderTotalPrice = 0;
            UUID customerId = null;
           for (OrderPayload orderPayload:orders){
               Optional<Product> productOptional = productRepo.findById(orderPayload.getProductId());
              if (productOptional.isPresent()){
                  float totalPrice = calculateTotalPrice(productOptional.get().getUnitPrice(), orderPayload.getQuantity());
                  entireOrderTotalPrice +=totalPrice;
                  customerId = orderPayload.getCustomerId();
              }

           }
           // saving to the order table
           Order generalOrder = new Order();
           generalOrder.setTotalPrice(entireOrderTotalPrice);
           generalOrder.setStatus(OrderStatus.PENDING.toString());
           generalOrder.setCustomerId(customerId);
           Order oderRes = orderRepo.save(generalOrder);

           // to hold data for individual product order details
           List<ProductOrder>res = new ArrayList<>();
           // to hold payload that will be sent to the notification service
            List<Object> notificationOrderProducts = new ArrayList<>();

            // looping and setting specific product order
            for (OrderPayload order: orders){
               Optional<Product> productOptional = productRepo.findById(order.getProductId());
               if (productOptional.isEmpty()){
                   log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                   ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                   return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
               }

               // checking for product availability
               Product product = productOptional.get();
               if (product.getQuantity()==0){
                   log.error("\"product out of stock:->>\" + \" \" + product.getName(){}", HttpStatus.BAD_REQUEST);
                   ResponseDTO  response = AppUtils.getResponseDto("order quantity exceeds product availability for:->>" + " " + product.getName(), HttpStatus.BAD_REQUEST);
                   return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
               }
               // checking if the order quantity exceed the available quantity of product
               if (order.getQuantity()>product.getQuantity()){
                   log.error("order quantity exceeds product availability for:-->>>>{}" + " " + product.getName(), HttpStatus.BAD_REQUEST);
                   ResponseDTO  response = AppUtils.getResponseDto("order quantity exceeds product availability for:->>" + " " + product.getName(), HttpStatus.BAD_REQUEST);
                   return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
               }

               // deducting the order quantity from the product quantity to get the remaining quantity.
               int productQuantityRemaining = product.getQuantity()- order.getQuantity();
               product.setQuantity(productQuantityRemaining);

               // calculating the product order total price
               float productOrderTotalPrice = calculateTotalPrice(product.getUnitPrice(), order.getQuantity());

               // setting the specific product order updated details
               ProductOrder productOrder = new ProductOrder();
               productOrder.setTotalPrice(productOrderTotalPrice);
               productOrder.setUnitPrice(product.getUnitPrice());
               productOrder.setProductId(order.getProductId());
               productOrder.setQuantity(order.getQuantity());
               productOrder.setOrderId(oderRes.getId());
               ProductOrder productOrderRes = productOrderRepo.save(productOrder);
               res.add(productOrderRes);
               Product productRes = productRepo.save(product);

                Map<String, Object> notificationMap = new HashMap<>();
                notificationMap.put("product", productRes.getName());
                notificationMap.put("price", productOrderRes.getUnitPrice());
                notificationMap.put("quantity", productOrderRes.getQuantity());
                notificationOrderProducts.add(notificationMap);
           }

           // send notification on success to customer
            User user = userRepo.findById(oderRes.getCustomerId())
                    .orElseThrow(()-> new NotFoundException("user record not found!"));
            OrderNotificationPayload orderNotificationPayload = OrderNotificationPayload
                    .builder()
                    .email(user.getEmail())
                    .fullName(AppUtils.getFullName(user.getFirstName(), user.getLastName()))
                    .products(notificationOrderProducts)
                    .orderTotals(oderRes.getTotalPrice())
                    .build();
            orderNotificationService.SendToCustomer(orderNotificationPayload);

            ResponseDTO  response = AppUtils.getResponseDto("product ordered successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private float calculateTotalPrice(float price, int quantity){
        return price*quantity;
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
            List<OrderProjection> orders = orderRepo.getOrderDetailsByUserId(userId);
            if (orders.isEmpty()){
                log.error("no order record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no order record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Map<Object, List<Object>> ordersObj = new HashMap<>();
            List<Object> res = new ArrayList<>();

            for (OrderProjection order : orders){
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
            }
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
    public ResponseEntity<ResponseDTO> updateOrder(OrderPayload orderPayload) {
       try{
           log.info("In update order method:->>>>>>");
           Optional<Order> orderOptional = orderRepo.findById(orderPayload.getOrderId());
           if (orderOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no order record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           Order existingOrder = orderOptional.get();

           Optional<Product> productOptional = productRepo.findById(orderPayload.getProductId());
           if (productOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           Product existingProduct = productOptional.get();

           ProductOrder productOrder = productOrderRepo.findByOrderId(orderPayload.getOrderId());
           if (productOrder == null){
               ResponseDTO  response = AppUtils.getResponseDto("no product order record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }

           float updatedProductOrderTotalPrice = calculateTotalPrice(existingProduct.getUnitPrice(), orderPayload.getQuantity());
           int previousProductQuantity = productOrder.getQuantity()+existingProduct.getQuantity();

           if (existingProduct.getQuantity()==0){
               log.error("product out of stock:->>>>>>{}", HttpStatus.BAD_REQUEST);
               ResponseDTO  response = AppUtils.getResponseDto("product out of stock", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }

           if (orderPayload.getQuantity()>previousProductQuantity){
               log.error("\"order quantity exceeds product availability for:->>\" + \" \" + product.getName(){}", HttpStatus.BAD_REQUEST);
               ResponseDTO  response = AppUtils.getResponseDto("order quantity exceeds product availability for:->>" + " " + existingProduct.getName(), HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }
           int updatedProductQuantity = previousProductQuantity - orderPayload.getQuantity();

           productOrder.setTotalPrice(updatedProductOrderTotalPrice);
           productOrder.setQuantity(updatedProductQuantity);
           productOrderRepo.save(productOrder);

           float orderTotalPrice = existingOrder.getTotalPrice()-productOrder.getTotalPrice();
           float updatedOrderTotalPrice = orderTotalPrice + updatedProductOrderTotalPrice;

           existingOrder.setTotalPrice(updatedOrderTotalPrice);
           orderRepo.save(existingOrder);

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
