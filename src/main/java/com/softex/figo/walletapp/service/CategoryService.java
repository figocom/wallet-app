package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.dto.CategoryDto;
import com.softex.figo.walletapp.repository.CategoryRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public WebResponse<?> createCategory(CategoryDto categoryDTO) {
        String type = categoryDTO.getType();
        if (!type.equals("income") && !type.equals("expense")) {
            return new WebResponse<>(new ErrorDTO("Type is not income or expense", 400));
        }
        Category.Type type1 = type.equalsIgnoreCase("income") ? Category.Type.INCOME : Category.Type.EXPENSE;
        if (categoryRepository.existsByNameEqualsIgnoreCaseAndTypeAndDeleted(categoryDTO.getName(), type1,false)) {
            return new WebResponse<>(new ErrorDTO("Category already exists", 400));
        }
        categoryRepository.save(new Category(categoryDTO.getName(), categoryDTO.getIcon(), type1));
        return new WebResponse<>(new DataDTO<>("Category successfully created"));
    }

    public WebResponse<?> getCategory(Long categoryId) {
        Optional<Category> byId = categoryRepository.findById(categoryId);
        if (byId.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Category not found", 404));
        }
        return new WebResponse<>(new DataDTO<>(byId.get()));
    }

    public WebResponse<?> getCategoryByType(String type) {
        if (type.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        if (!type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expense")) {
            return new WebResponse<>(new ErrorDTO("Type is not income or expense", 400));
        }
        Category.Type type1 = Category.Type.valueOf(type.toUpperCase());
        Optional<List<Category>> byName = categoryRepository.findAllByTypeAndDeleted(type1, false);
        if (byName.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Category not found", 404));
        }
        return new WebResponse<>(new DataDTO<>(byName.get()));
    }

    public WebResponse<?> updateCategory(Long categoryId, CategoryDto categoryDTO) {
        Optional<Category> byId = categoryRepository.findByIdAndDeleted(categoryId, false);
        if (byId.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Category not found", 404));
        }
        String type = categoryDTO.getType();
        if (!type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expense")) {
            return new WebResponse<>(new ErrorDTO("Type is not income or expense", 400));
        }
        Category.Type type1 = Category.Type.valueOf(type.toUpperCase());
        if (categoryRepository.existsByNameEqualsIgnoreCaseAndTypeAndDeleted(categoryDTO.getName(), type1,false)) {
            return new WebResponse<>(new ErrorDTO("Category already exists", 400));
        }
        byId.get().setName(categoryDTO.getName());
        byId.get().setIcon(categoryDTO.getIcon());
        byId.get().setType(type1);
        categoryRepository.save(byId.get());
        return new WebResponse<>(new DataDTO<>("Category successfully updated"));
    }

    public WebResponse<?> deleteCategory(Long categoryId) {
        Optional<Category> byId = categoryRepository.findByIdAndDeleted(categoryId, false);
        if (byId.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Category not found", 404));
        }
        byId.get().setDeleted(true);
        categoryRepository.save(byId.get());
        return new WebResponse<>(new DataDTO<>("Category successfully deleted"));
    }
}
