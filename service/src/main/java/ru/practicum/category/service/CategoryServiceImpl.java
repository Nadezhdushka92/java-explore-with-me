package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(CreateCategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Name should be unique", "");
        }

        Category createdCategory = CategoryMapper.mapCategory(categoryDto);
        categoryRepository.save(createdCategory);

        log.info("Create new category {}", createdCategory);
        return CategoryMapper.mapCategoryDto(createdCategory);
    }

    @Override
    public void deleteCategory(int catId) {
        categoryRepository.findById(catId).orElseThrow(() ->
                new ConflictException("Category with id=" + catId + " was not found",
                        "The required object was not found."));

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Exists events, assigned with this category", "");
        }

        log.info("Delete category id {}", catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Category is not found", ""));

        if (categoryRepository.existsByName(categoryDto.getName())
            && !categoryDto.getName().equals(category.getName())) {
            throw new ConflictException("Name should be unique", "");
        }

        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);

        log.info("Update info about category with catId {}", catId);
        return CategoryMapper.mapCategoryDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        log.info("Get categories from Id {}, size {}", from, size);
        return categoryPage.getContent().stream()
                .map(CategoryMapper::mapCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category is not found", ""));

        log.info("Get categories from id {}", id);
        return CategoryMapper.mapCategoryDto(category);
    }
}
