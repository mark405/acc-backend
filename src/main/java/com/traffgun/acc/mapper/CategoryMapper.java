package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.category.CategoryResponse;
import com.traffgun.acc.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public CategoryResponse toDto(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getComment()
        );
    }
}
