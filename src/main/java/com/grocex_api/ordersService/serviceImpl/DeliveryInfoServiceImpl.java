package com.grocex_api.ordersService.serviceImpl;

import com.grocex_api.ordersService.dto.OrderStatus;
import com.grocex_api.ordersService.models.DeliveryInfo;
import com.grocex_api.ordersService.models.Order;
import com.grocex_api.ordersService.repo.DeliveryInfoRepo;
import com.grocex_api.ordersService.repo.OrderRepo;
import com.grocex_api.ordersService.service.DeliveryInfoService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserDTOProjection;
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

@Slf4j
@Service
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

    private final DeliveryInfoRepo deliveryInfoRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public DeliveryInfoServiceImpl(DeliveryInfoRepo deliveryInfoRepo, OrderRepo orderRepo) {
        this.deliveryInfoRepo = deliveryInfoRepo;
        this.orderRepo = orderRepo;
    }

    /**
     * @description This method is used to save delivery info record for an order.
     * @param deliveryInfo
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> saveDeliveryInfo(DeliveryInfo deliveryInfo) {
      try{
          log.info("In add delivery info method:->>>>>>>>");
          if (deliveryInfo == null){
              log.error("delivery info payload cannot be be null{}", HttpStatus.BAD_REQUEST);
              ResponseDTO  response = AppUtils.getResponseDto("delivery infopayload cannot be null", HttpStatus.BAD_REQUEST);
              return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
          }

          Optional<Order> orderOptional = orderRepo.findById(deliveryInfo.getOrderId());
          if (orderOptional.isEmpty()){
              log.error("order record not found:->>>>{}", HttpStatus.NOT_FOUND);
              ResponseDTO  response = AppUtils.getResponseDto("order record not found", HttpStatus.NOT_FOUND);
              return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
          }

          // update order status to processing
          Order existingOrder = orderOptional.get();
          existingOrder.setStatus(OrderStatus.PROCESSING.toString());
          orderRepo.save(existingOrder);

          DeliveryInfo deliveryInfoRes = deliveryInfoRepo.save(deliveryInfo);
          ResponseDTO  response = AppUtils.getResponseDto("product ordered successfully", HttpStatus.CREATED, deliveryInfoRes);
          return new ResponseEntity<>(response, HttpStatus.OK);
      }catch (Exception e) {
          log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
          ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    /**
     * @description This method is used to fetch all delivery info records from the db.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In get all users method:->>>>>>");
            List<DeliveryInfo> deliveryInfos = deliveryInfoRepo.findAll();
            if (deliveryInfos.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no delivery info record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            ResponseDTO  response = AppUtils.getResponseDto("delivery info records fetched successfully", HttpStatus.OK, deliveryInfos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get delivery info record given the id.
     * @param deliveryInfoId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> finDeliveryInfoById(UUID deliveryInfoId) {
        try{
            log.info("In get delivery info by id method:->>>>>>");
            Optional<DeliveryInfo> deliveryInfoOptional = deliveryInfoRepo.findById(deliveryInfoId);
            if (deliveryInfoOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no delivery inforecord found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            DeliveryInfo deliveryInfoRes = deliveryInfoOptional.get();
            ResponseDTO  response = AppUtils.getResponseDto("delivery info records fetched successfully", HttpStatus.OK, deliveryInfoRes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get delivery info for an order give the user id. the user here is the customer who placed the order.
     * @param userId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findDeliveryInfoByUserId(UUID userId) {
        try{
            log.info("In get delivery info by user id method:->>>>>>");
            Optional<DeliveryInfo> deliveryInfoOptional = deliveryInfoRepo.findById(userId);
            if (deliveryInfoOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no delivery inforecord found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            DeliveryInfo deliveryInfoRes = deliveryInfoOptional.get();
            ResponseDTO  response = AppUtils.getResponseDto("delivery info records fetched successfully", HttpStatus.OK, deliveryInfoRes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update delivery info records for an order given the id.
     * @param deliveryInfo
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateDeliveryInfo(DeliveryInfo deliveryInfo, UUID deliveryInfoId) {
     try{
         log.info("In update delivery info method:->>>>>>");
         Optional<DeliveryInfo> deliveryInfoOptional = deliveryInfoRepo.findById(deliveryInfoId);
         if (deliveryInfoOptional.isEmpty()){
             ResponseDTO  response = AppUtils.getResponseDto("no delivery inforecord found", HttpStatus.NOT_FOUND);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         }
         DeliveryInfo existingData = deliveryInfoOptional.get();
         existingData.setContact(deliveryInfo.getContact());
         existingData.setEmail(deliveryInfo.getEmail());
         existingData.setLocation(deliveryInfo.getLocation());
         DeliveryInfo deliveryInfoRes = deliveryInfoRepo.save(existingData);
         ResponseDTO  response = AppUtils.getResponseDto("delivery info records updated successfully", HttpStatus.OK, deliveryInfoRes);
         return new ResponseEntity<>(response, HttpStatus.OK);
     }catch (Exception e) {
         log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
         ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
         return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }

    /**
     * @description This method is used to remove delivery info record for an order given the id.
     * @param deliveryInfoId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 4th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeDeliveryInfo(UUID deliveryInfoId) {
      try{
          log.info("In remove delivery info method:->>>>>>");
          Optional<DeliveryInfo> deliveryInfoOptional = deliveryInfoRepo.findById(deliveryInfoId);
          if (deliveryInfoOptional.isEmpty()){
              ResponseDTO  response = AppUtils.getResponseDto("no delivery inforecord found", HttpStatus.NOT_FOUND);
              return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
          }
          deliveryInfoRepo.deleteById(deliveryInfoOptional.get().getId());
          ResponseDTO  response = AppUtils.getResponseDto("delivery info records deleted successfully", HttpStatus.OK);
          return new ResponseEntity<>(response, HttpStatus.OK);
      }catch (Exception e) {
          log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
          ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
}
