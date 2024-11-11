package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ElementStatsResponseDto {
    @NotNull
    @NotEmpty
    private String app;
    @NotNull
    @NotEmpty
    private String uri;
    private Long hits;
}
