package com.grocex_api.productService.service;

import com.grocex_api.productService.models.Vendor;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface VendorService {
    ResponseEntity<ResponseDTO> findAll();
    void saveVendor(Vendor vendor);
    void updateVendor(Vendor vendor);
}
