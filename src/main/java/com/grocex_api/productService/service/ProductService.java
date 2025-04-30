package com.grocex_api.productService.service;

import com.grocex_api.productService.models.Product;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductService {

    ResponseEntity<ResponseDTO> saveProduct(Product product, MultipartFile file);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findProductById(UUID productId);
    ResponseEntity<ResponseDTO> updateProduct(Product product);
    ResponseEntity<ResponseDTO> removeProduct(UUID productId);
}
