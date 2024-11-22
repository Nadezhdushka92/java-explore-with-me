package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.model.Category;

public class CategoryMapper {
    public static CategoryDto mapCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category mapCategory(CreateCategoryDto createCategoryDto) {
        return Category.builder()
                .name(createCategoryDto.getName())
                .build();
    }
}
