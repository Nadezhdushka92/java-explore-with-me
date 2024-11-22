package ru.practicum.category.publicController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return publicCategoryService.getCategories(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable int id) {
        return publicCategoryService.getCategory(id);
    }
}
