package ru.practicum.stats;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
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
