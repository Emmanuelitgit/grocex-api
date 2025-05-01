package com.grocex_api.productService.rest;

import com.grocex_api.productService.models.Category;
import com.grocex_api.productService.serviceImpl.CategoryServiceImpl;
import com.grocex_api.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRest {

    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryRest(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveCategory(@RequestBody Category category){
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

    @PutMapping
    public ResponseEntity<ResponseDTO> updateCategory(Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> removeCategory(@RequestBody Category category){
        return categoryService.removeCategory(category);
    }
}
