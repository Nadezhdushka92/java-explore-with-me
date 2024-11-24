package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Map;
import java.util.Set;

public class CompilationMapper {

    public static CompilationDto toDto(Compilation compilation) {

        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents() != null
                        ? compilation.getEvents().stream()
                        .map(event -> EventMapper.mapToEventShortDto(event, Map.of(), Map.of())).toList() : null)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation createToCompilation(CreateCompilationDto createCompilation, Set<Event> events) {

        return Compilation.builder()
                .events(events)
                .pinned(createCompilation.getPinned() != null ? createCompilation.getPinned() : false)
                .title(createCompilation.getTitle())
                .build();
    }

    public static Compilation updateCompilation(UpdateCompilationRequest updateCompilation, Compilation oldCompilation,
                                                Set<Event> events) {

        return Compilation.builder()
                .id(oldCompilation.getId())
                .events(events != null
                        ? events
                        : oldCompilation.getEvents())
                .pinned(updateCompilation.getPinned() != null
                        ? updateCompilation.getPinned()
                        : oldCompilation.getPinned())
                .title(updateCompilation.getTitle() != null
                        ? updateCompilation.getTitle()
                        : oldCompilation.getTitle())
                .build();
    }
}
