package com.influy.domain.category.service;

import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
