package com.grocex_api.productService.service;

import com.grocex_api.productService.models.Category;
import com.grocex_api.response.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryService {
    ResponseEntity<ResponseDTO> saveCategory(Category category);
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findCategoryById(UUID categoryId);
    ResponseEntity<ResponseDTO> updateCategory(Category category, UUID categoryId);
    ResponseEntity<ResponseDTO> removeCategory(UUID categoryId);
}
