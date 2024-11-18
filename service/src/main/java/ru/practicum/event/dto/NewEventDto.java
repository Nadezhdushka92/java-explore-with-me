package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.PositiveOrZero;

@SuppressWarnings("checkstyle:Regexp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewEventDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @Positive
    @NotNull
    private Integer category;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private LocationDto location;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
}