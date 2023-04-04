package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.dto.CategoryDto;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<WebResponse<?>> createCategory(@Valid @RequestBody CategoryDto categoryDTO) {
        WebResponse<?> category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(category.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(category);
    }

    @GetMapping("/get/{categoryId}")
    public ResponseEntity<WebResponse<?>> getCategory(@NonNull @PathVariable Long categoryId) {
        WebResponse<?> category = categoryService.getCategory(categoryId);
        return ResponseEntity.status(category.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(category);
    }

    @GetMapping("/getByCategory/{type}")
    public ResponseEntity<WebResponse<?>> getCategoryByType(@NonNull @PathVariable String type) {
        WebResponse<?> categoryByType = categoryService.getCategoryByType(type);
        return ResponseEntity.status(categoryByType.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(categoryByType);
    }
    @PatchMapping ("/update/{categoryId}")
    public ResponseEntity<WebResponse<?>> updateCategory(@NonNull @PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDTO) {
        WebResponse<?> category = categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.status(category.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(category);
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<WebResponse<?>> deleteCategory(@NonNull @PathVariable Long categoryId) {
        WebResponse<?> category = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(category.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(category);
    }

}
