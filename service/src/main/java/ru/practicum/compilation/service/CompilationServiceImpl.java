package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto createCompilation(CreateCompilationDto createCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (createCompilationDto.getEvents() != null && !createCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findByIdIn(createCompilationDto.getEvents());
        }

        Compilation compilationBuilder = CompilationMapper.createToCompilation(createCompilationDto, events);

        Compilation compilation = compilationRepository.save(compilationBuilder);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilation) {
        Compilation oldCompilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation not found", ""));

        Set<Event> events = (updateCompilation.getEvents() != null && !updateCompilation.getEvents().isEmpty())
                ? eventRepository.findByIdIn(updateCompilation.getEvents())
                : null;

        Compilation compilation = CompilationMapper.updateCompilation(updateCompilation, oldCompilation, events);

        Compilation updatedCompilation = compilationRepository.save(compilation);

        return CompilationMapper.toDto(updatedCompilation);
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation not found", ""));

        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getListCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Compilation> page = compilationRepository.findByPinned(pinned, pageable);

        return page.stream()
                .map(CompilationMapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ValidationException("Compilation with id=" + compId + " was not found",
                        "The required object was not found."));

        return CompilationMapper.toDto(compilation);
    }
}
