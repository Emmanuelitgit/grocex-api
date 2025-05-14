package com.grocex_api.productService.rest;

import com.grocex_api.productService.service.VendorService;
import com.grocex_api.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
