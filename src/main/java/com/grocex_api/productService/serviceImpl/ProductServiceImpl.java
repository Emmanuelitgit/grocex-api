package com.grocex_api.productService.serviceImpl;

import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.repo.ProductRepo;
import com.grocex_api.productService.service.ProductService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * @description This method is used to save product to the db
     * @param product
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> saveProduct(Product product) {
        try{
            log.info("In save product method:->>>>>>>");
            if (product == null){
                log.error("payload cannot be null:->>>>>>>{}", HttpStatus.BAD_REQUEST);
                ResponseDTO  response = AppUtils.getResponseDto("product payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            product.setCreatedAt(ZonedDateTime.now());
            Product productRes = productRepo.save(product);
//            productFileService.saveProductFile(file);
            ResponseDTO  response = AppUtils.getResponseDto("product record added successfully", HttpStatus.CREATED, productRes);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get all products from the db
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In get all products method:->>>>>>");
            List<ProductProjection> products = productRepo.getProductAndCategory();
            if (products.isEmpty()){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("products records fetched successfully", HttpStatus.OK, products);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get product records given the product id.
     * @param productId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findProductById(UUID productId) {
        try{
            log.info("In get product by id method:->>>>>>");
            ProductProjection product = productRepo.getProductAndCategoryById(productId);
            if (product == null){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("user records fetched successfully", HttpStatus.OK, product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update product records.
     * @param product
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateProduct(Product product) {
       try {
           log.info("In update product method:->>>>>>");
           Optional<Product> productOptional = productRepo.findById(product.getId());
           if (productOptional.isEmpty()){
               log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
               ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }

           Product existingData = productOptional.get();
           existingData.setName(product.getName());
           existingData.setFileId(product.getFileId());
           existingData.setCategoryId(product.getCategoryId());
           existingData.setRatingId(product.getRatingId());
           existingData.setProductOwnerId(product.getProductOwnerId());
           existingData.setUnitPrice(product.getUnitPrice());
           existingData.setQuantity(product.getQuantity());
           Product productRes = productRepo.save(existingData);

           ResponseDTO  response = AppUtils.getResponseDto("product records updated successfully", HttpStatus.OK, productRes);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to remove product records from the db.
     * @param productId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30th April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeProduct(UUID productId) {
        try {
            log.info("In remove product method:->>>>>>{}", productId);
            Optional<Product> productOptional = productRepo.findById(productId);
            if (productOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            productRepo.deleteById(productId);
            log.info("product removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("product record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
