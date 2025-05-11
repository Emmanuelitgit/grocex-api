package com.grocex_api.productService.rest;

import com.grocex_api.imageUtility.ImageUtil;
import com.grocex_api.productService.dto.ProductRequest;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.repo.ProductRepo;
import com.grocex_api.productService.serviceImpl.ProductServiceImpl;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserPayloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductRest {

    private final ProductServiceImpl productService;
    private final ProductRepo productRepo;

    @Autowired
    public ProductRest(ProductServiceImpl productService, ProductRepo productRepo) {
        this.productService = productService;
        this.productRepo = productRepo;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return productService.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Integer price,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("categoryId") UUID category,
            @RequestParam("file")  MultipartFile file) throws IOException {

        Product data = Product.builder()
                .image(ImageUtil.compressImage(file.getBytes()))
                .name(name)
                .unitPrice(price)
                .quantity(quantity)
                .productOwnerId(ownerId)
                .categoryId(category)
//                .categoryId()
                .build();
        return productService.saveProduct(data);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDTO> findProductById(@PathVariable UUID productId){
        return productService.findProductById(productId);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO> findProductByCategory(@PathVariable String category){
        return productService.findProductByCategory(category);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removeProduct(@RequestBody Product product){
        return productService.removeProduct(product.getId());
    }

    @GetMapping("/vendor/{vendor}")
    public ResponseEntity<ResponseDTO> findProductByVendor(@PathVariable String vendor){
        return productService.findProductByVendor(vendor);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?>  getImageByName(@PathVariable UUID id){
        byte[] image = productService.getImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }
}
