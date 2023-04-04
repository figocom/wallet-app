package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.dto.SubCategoryDto;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subcategory")
@RequiredArgsConstructor
public class SubCategoryController {
    private final SubcategoryService subcategoryService;

    @PostMapping("/create")
    public ResponseEntity<WebResponse<?>> createSubCategory(@Valid @RequestBody SubCategoryDto subCategoryDto) {
        WebResponse<?> response = subcategoryService.createSubCategory(subCategoryDto);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 201).body(response);
    }
    @GetMapping("/get/{subcategoryId}")
    public ResponseEntity<WebResponse<?>> getSubCategory(@PathVariable Long subcategoryId){
        WebResponse<?> response = subcategoryService.getSubcategory(subcategoryId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-all")
    public ResponseEntity<WebResponse<?>> getAllSubCategory(){
        WebResponse<?> response = subcategoryService.getAllSubcategory();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/update/{subcategoryId}")
    public ResponseEntity<WebResponse<?>> updateSubcategory(@PathVariable Long subcategoryId , @Valid @RequestBody SubCategoryDto subCategoryDto){
        WebResponse<?>response = subcategoryService.updateSubcategory(subcategoryId, subCategoryDto);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 201).body(response);
    }
    @DeleteMapping("/delete/{subcategoryId}")
    public ResponseEntity<WebResponse<?>> deleteSubcategory(@PathVariable Long subcategoryId){
        WebResponse<?> response = subcategoryService.deleteSubcategory(subcategoryId);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 201).body(response);
    }


}
