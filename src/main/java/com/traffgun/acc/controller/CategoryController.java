package com.traffgun.acc.controller;

import com.traffgun.acc.dto.category.CategoryResponse;
import com.traffgun.acc.dto.category.CreateCategoryRequest;
import com.traffgun.acc.dto.category.UpdateCategoryRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.CategoryMapper;
import com.traffgun.acc.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return categoryMapper.toDto(category);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll().stream().map(categoryMapper::toDto).toList();
    }

    @PostMapping()
    public CategoryResponse createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        return categoryMapper.toDto(categoryService.create(request));
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable("id") Long id, @RequestBody @Valid UpdateCategoryRequest request) {
        Category category = categoryService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Category updatedCategory = categoryService.update(category, request);
        return categoryMapper.toDto(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
