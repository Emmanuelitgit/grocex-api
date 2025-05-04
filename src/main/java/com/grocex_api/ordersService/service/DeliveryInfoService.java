package com.grocex_api.ordersService.service;

import com.grocex_api.ordersService.dto.OrderPayload;
import com.grocex_api.ordersService.models.DeliveryInfo;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface DeliveryInfoService {

    ResponseEntity<ResponseDTO> saveDeliveryInfo(DeliveryInfo deliveryInfo);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> finDeliveryInfoById(UUID deliveryInfoId);
    ResponseEntity<ResponseDTO> findDeliveryInfoByUserId(UUID userId);
    ResponseEntity<ResponseDTO> updateDeliveryInfo(DeliveryInfo deliveryInfo);
    ResponseEntity<ResponseDTO> removeDeliveryInfo(DeliveryInfo deliveryInfo);
}
