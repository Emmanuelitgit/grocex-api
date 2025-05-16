package com.grocex_api.productService.rest;

import com.grocex_api.imageUtility.ImageUtil;
import com.grocex_api.productService.dto.PaginationPayload;
import com.grocex_api.productService.dto.ProductRequest;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.serviceImpl.ProductServiceImpl;
import com.grocex_api.response.ResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductRest {

    private final ProductServiceImpl productService;

    @Autowired
    public ProductRest(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "paginate", defaultValue = "false", required = false) boolean paginate
    ){

        PaginationPayload paginationPayload = PaginationPayload
                .builder()
                .page(page)
                .paginate(paginate)
                .size(size)
                .build();
        return productService.findAll(paginationPayload);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveProduct(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        Product data = Product.builder()
                .image(ImageUtil.compressImage(productRequest.getFile().getBytes()))
                .name(productRequest.getName())
                .unitPrice(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .productOwnerId(productRequest.getOwnerId())
                .categoryId(productRequest.getCategoryId())
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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateProduct(
            @RequestParam(value = "id") UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Float price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "ownerId", required = false) UUID ownerId,
            @RequestParam(value = "categoryId", required = false) UUID category,
            @RequestParam(value = "file", required = false)  MultipartFile file
    ) throws IOException {
        Product data = Product.builder()
                .id(id)
                .image(file != null ? file.getBytes() : null)
                .name(name)
                .unitPrice(price)
                .quantity(quantity !=null? quantity : 0)
                .productOwnerId(ownerId)
                .categoryId(category)
                .categoryId(category)
                .build();
        return productService.updateProduct(data);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseDTO> removeProduct(@PathVariable UUID productId){
        return productService.removeProduct(productId);
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