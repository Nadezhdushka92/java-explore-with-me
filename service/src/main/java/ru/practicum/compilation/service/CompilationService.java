package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CreateCompilationDto updateCompilation);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilation);

    void deleteCompilation(int compId);

    List<CompilationDto> getListCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(int compId);

}
