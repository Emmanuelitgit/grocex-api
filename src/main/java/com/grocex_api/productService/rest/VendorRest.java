package com.grocex_api.productService.rest;

import com.grocex_api.productService.models.Vendor;
import com.grocex_api.productService.service.VendorService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorRest {

    private final VendorService vendorService;

    @Autowired
    public VendorRest(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/vendors")
    public ResponseEntity<ResponseDTO> getVendors(){
        return vendorService.findAll();
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateVendor(Vendor vendor){
        vendorService.updateVendor(vendor);
        ResponseDTO  response = AppUtils.getResponseDto("order records fetched successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
