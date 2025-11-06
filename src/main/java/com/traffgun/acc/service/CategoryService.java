package com.traffgun.acc.service;

import com.traffgun.acc.dto.category.CreateCategoryRequest;
import com.traffgun.acc.dto.category.UpdateCategoryRequest;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.repository.CategoryRepository;
import com.traffgun.acc.repository.OperationRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Transactional
    public void deleteById(Long id) {
        operationRepository.deleteByCategoryId(id);
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category create(CreateCategoryRequest request) {
        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .comment(request.getComment())
                .build()
        );
    }

    @Transactional
    public Category update(Category category, UpdateCategoryRequest request) {
        category.setName(request.getName());
        category.setComment(request.getComment());
        return categoryRepository.save(category);
    }
}
