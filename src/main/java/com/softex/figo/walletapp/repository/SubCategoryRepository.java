package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsByNameEqualsIgnoreCaseAndCategory(String name, Category category);


    Optional<SubCategory> findByIdAndDeleted(Long subcategoryId, boolean b);

    SubCategory findAllByDeleted(boolean b);
}
