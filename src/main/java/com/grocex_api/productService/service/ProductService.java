package com.grocex_api.productService.service;

import com.grocex_api.productService.dto.PaginationPayload;
import com.grocex_api.productService.dto.ProductRequest;
import com.grocex_api.productService.dto.UpdateProductPayload;
import com.grocex_api.productService.models.Product;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

public interface ProductService {

    ResponseEntity<ResponseDTO> saveProduct(ProductRequest productRequest);
    ResponseEntity<ResponseDTO> findAll(PaginationPayload paginationPayload);
    ResponseEntity<ResponseDTO> findProductById(UUID productId);
    ResponseEntity<ResponseDTO> updateProduct(UpdateProductPayload updateProductPayload);
    ResponseEntity<ResponseDTO> removeProduct(UUID productId);
    ResponseEntity<ResponseDTO> findProductByCategory(String category);
    ResponseEntity<ResponseDTO> findProductByVendor(String vendor);
}
