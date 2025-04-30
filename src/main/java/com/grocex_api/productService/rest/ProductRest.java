package com.grocex_api.productService.rest;

import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.serviceImpl.ProductServiceImpl;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserPayloadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRest {

    private final ProductServiceImpl productService;

    @Autowired
    public ProductRest(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return productService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveProduct(@RequestBody Product product, @RequestParam MultipartFile file){
        return productService.saveProduct(product, file);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDTO> findProductById(@PathVariable UUID productId){
        return productService.findProductById(productId);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removeProduct(@RequestBody Product product){
        return productService.removeProduct(product.getId());
    }
}
