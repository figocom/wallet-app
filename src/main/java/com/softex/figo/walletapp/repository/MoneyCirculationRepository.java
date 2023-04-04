package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.domain.MoneyCirculation;
import com.softex.figo.walletapp.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MoneyCirculationRepository extends JpaRepository<MoneyCirculation, Long> {
    List<MoneyCirculation> findAllByTypeAndAuthUser(Category.Type type, AuthUser authUser);

    List<MoneyCirculation> findAllByAuthUser(AuthUser authUser);

    List<MoneyCirculation> findAllByCategoryAndAuthUserAndType(Category category, AuthUser currentUser, Category.Type categoryType);

    List<MoneyCirculation> findAllBySubCategoryAndAuthUserAndType(SubCategory subCategory, AuthUser currentUser, Category.Type categoryType);

    List<MoneyCirculation> findAllByAuthUserAndType(AuthUser currentUser, Category.Type categoryType);

    List<MoneyCirculation> findAllByAuthUserAndTypeAndCategory(AuthUser currentUser, Category.Type categoryType, Category category);

    List<MoneyCirculation> findAllByAuthUserAndCategory(AuthUser currentUser, Category category);

    List<MoneyCirculation> findAllByAuthUserAndCreatedAt(AuthUser currentUser, LocalDateTime date);

    List<MoneyCirculation> findAllByAuthUserAndTypeAndCreatedAt(AuthUser currentUser, Category.Type categoryType, LocalDateTime from);

    List<MoneyCirculation> findAllByAuthUserAndCategoryAndCreatedAt(AuthUser currentUser, Category category, LocalDateTime from);

    List<MoneyCirculation> findAllByAuthUserAndTypeAndCategoryAndCreatedAt(AuthUser currentUser, Category.Type categoryType, Category category, LocalDateTime from);
}
