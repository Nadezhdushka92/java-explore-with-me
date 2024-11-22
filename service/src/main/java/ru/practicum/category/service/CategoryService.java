package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;

import java.util.List;

@SuppressWarnings("checkstyle:Regexp")
public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto categoryDto);

    void deleteCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int id);
}
