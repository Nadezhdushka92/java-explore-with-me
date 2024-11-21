package ru.practicum.event.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("checkstyle:Regexp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateEventDto {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @Positive
    @NotNull
    private Integer category;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @NotEmpty
    @NotBlank
    private String eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
}