package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {



    Optional<List<Category>> findAllByType(Category.@NonNull Type type);


    Optional<Category> findByIdAndDeleted(Long categoryId, boolean b);
    @Transactional
    boolean existsByNameEqualsIgnoreCaseAndTypeAndDeleted(String name, Category.Type type1, boolean b);

    Optional<List<Category>> findAllByTypeAndDeleted(Category.Type type1, boolean b);
}
