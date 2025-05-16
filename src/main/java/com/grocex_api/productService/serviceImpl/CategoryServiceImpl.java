package com.grocex_api.productService.serviceImpl;

import com.grocex_api.productService.models.Category;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.repo.CategoryRepo;
import com.grocex_api.productService.service.CategoryService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveCategory(Category category) {
        try{
            log.info("In save category method:->>>>>>>");
            if (category == null){
                log.error("payload cannot be null:->>>>>>>{}", HttpStatus.BAD_REQUEST);
                ResponseDTO  response = AppUtils.getResponseDto("product payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            category.setCreatedAt(ZonedDateTime.now());
            Category categoryRes = categoryRepo.save(category);
            ResponseDTO  response = AppUtils.getResponseDto("category record added successfully", HttpStatus.CREATED, categoryRes);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In get all users method:->>>>>>");
            List<Category> categories = categoryRepo.findAll();
            if (categories.isEmpty()){
                log.error("no category record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no category record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("categories records fetched successfully", HttpStatus.OK, categories);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findCategoryById(UUID categoryId) {
        try{
            log.info("In get category by id method:->>>>>>");
            Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
            if (categoryOptional.isEmpty()){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("user records fetched successfully", HttpStatus.OK, categoryOptional.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> updateCategory(Category category, UUID categoryId) {
        log.info("In update category method:->>>>>>");
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        if (categoryOptional.isEmpty()){
            log.error("no category record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
            ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Category existingData = categoryOptional.get();
        existingData.setName(category.getName());
        existingData.setUpdatedAt(ZonedDateTime.now());
        categoryRepo.save(existingData);
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeCategory(UUID categoryId) {
        try {
            log.info("In remove product method:->>>>>>{}", categoryId);
            Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
            if (categoryOptional.isEmpty()){
                log.error("no category record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no category record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            categoryRepo.deleteById(categoryId);
            log.info("category removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("category record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
