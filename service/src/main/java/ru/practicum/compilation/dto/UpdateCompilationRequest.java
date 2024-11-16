package ru.practicum.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateCompilationRequest {
    private List<Integer> events;
    private Boolean pinned;

    @Size(min = 3, max = 50)
    private String title;
}
