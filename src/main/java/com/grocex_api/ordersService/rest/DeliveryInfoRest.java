package com.grocex_api.ordersService.rest;

import com.grocex_api.ordersService.models.DeliveryInfo;
import com.grocex_api.ordersService.service.DeliveryInfoService;
import com.grocex_api.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery-info")
public class DeliveryInfoRest {

    private final DeliveryInfoService deliveryInfoService;

    @Autowired
    public DeliveryInfoRest(DeliveryInfoService deliveryInfoService) {
        this.deliveryInfoService = deliveryInfoService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveDeliveryInfo(@RequestBody DeliveryInfo deliveryInfo){
        return deliveryInfoService.saveDeliveryInfo(deliveryInfo);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return deliveryInfoService.findAll();
    }

    @GetMapping("/{deliveryInfoId}")
    public ResponseEntity<ResponseDTO> finDeliveryInfoById(@PathVariable UUID deliveryInfoId){
        return deliveryInfoService.finDeliveryInfoById(deliveryInfoId);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO> findDeliveryInfoByUserId(@PathVariable UUID userId){
        return deliveryInfoService.findDeliveryInfoByUserId(userId);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateDeliveryInfo(@RequestBody DeliveryInfo deliveryInfo){
        return deliveryInfoService.updateDeliveryInfo(deliveryInfo);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removeDeliveryInfo(@RequestBody DeliveryInfo deliveryInfo){
        return deliveryInfoService.removeDeliveryInfo(deliveryInfo);
    }
}
