package com.influy.domain.category.repository;

import com.influy.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
    SELECT c
    FROM Category c
    JOIN ItemCategory ic ON ic.category = c
    WHERE ic.item.id = :itemId
    """)
    List<Category> findAllByItemId(@Param("itemId") Long itemId);
}
