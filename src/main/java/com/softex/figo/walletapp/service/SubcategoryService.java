package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.domain.SubCategory;
import com.softex.figo.walletapp.dto.SubCategoryDto;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.CategoryRepository;
import com.softex.figo.walletapp.repository.SubCategoryRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import org.springframework.stereotype.Service;

@Service
public class SubcategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryService(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public WebResponse<?> createSubCategory(SubCategoryDto subCategoryDto) {
        Long categoryId = Long.parseLong(subCategoryDto.getCategoryId());
        Category category;
        try {
           category= categoryRepository.findById(categoryId).orElseThrow(() -> new ItemNotFoundException("Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        boolean exists = subCategoryRepository.existsByNameEqualsIgnoreCaseAndCategory(subCategoryDto.getName(), category);
        if (exists){
            return new WebResponse<>(new ErrorDTO("Category already exist",409));
        }
        SubCategory subCategory= new SubCategory(subCategoryDto.getName(), subCategoryDto.getIcon(), category);
        SubCategory save = subCategoryRepository.save(subCategory);
        return new WebResponse<>(new DataDTO<>(save));
    }

    public WebResponse<?> getSubcategory(Long subcategoryId) {
        SubCategory category;
        try {
            category= subCategoryRepository.findByIdAndDeleted(subcategoryId, false).orElseThrow(() -> new ItemNotFoundException("Sub Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new WebResponse<>(new DataDTO<>(category));
    }


    public WebResponse<?> updateSubcategory(Long subcategoryId, SubCategoryDto subCategoryDto) {
        SubCategory subcategory;
        try {
            subcategory= subCategoryRepository.findByIdAndDeleted(subcategoryId,false).orElseThrow(() -> new ItemNotFoundException("Sub Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }

        Long categoryId = Long.parseLong(subCategoryDto.getCategoryId());
        Category category;
        try {
            category= categoryRepository.findById(categoryId).orElseThrow(() -> new ItemNotFoundException("Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        boolean exists = subCategoryRepository.existsByNameEqualsIgnoreCaseAndCategory(subCategoryDto.getName(), category);
        if (exists){
            return new WebResponse<>(new ErrorDTO("Category already exist",409));
        }
        subcategory.setIcon(subCategoryDto.getIcon());
        subcategory.setCategory(category);
        subcategory.setName(subCategoryDto.getName());
        SubCategory save = subCategoryRepository.save(subcategory);
        return new WebResponse<>(new DataDTO<>(save));
    }

    public WebResponse<?> deleteSubcategory(Long subcategoryId) {
        SubCategory subcategory;
        try {
            subcategory= subCategoryRepository.findById(subcategoryId).orElseThrow(() -> new ItemNotFoundException("Sub Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        subcategory.setDeleted(true);
        subCategoryRepository.save(subcategory);
        return new WebResponse<>(new DataDTO<>("Sub Category deleted successfully"));
    }

    public WebResponse<?> getAllSubcategory() {
        return new WebResponse<>(new DataDTO<>(subCategoryRepository.findAllByDeleted(false)));
    }
}
