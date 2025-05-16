package com.grocex_api.productService.rest;

import com.grocex_api.productService.models.Category;
import com.grocex_api.productService.serviceImpl.CategoryServiceImpl;
import com.grocex_api.productService.serviceImpl.ProductServiceImpl;
import com.grocex_api.response.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRest {

    private final CategoryServiceImpl categoryService;
    private final ProductServiceImpl productServiceImpl;

    @Autowired
    public CategoryRest(CategoryServiceImpl categoryService, ProductServiceImpl productServiceImpl) {
        this.categoryService = categoryService;
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveCategory(@RequestBody @Valid Category category){
        return categoryService.saveCategory(category);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> findCategoryById(@PathVariable UUID categoryId){
        return categoryService.findCategoryById(categoryId);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(@RequestBody @Valid Category category, @PathVariable UUID categoryId){
        return categoryService.updateCategory(category, categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> removeCategory(@PathVariable UUID categoryId){
        return categoryService.removeCategory(categoryId);
    }
}
