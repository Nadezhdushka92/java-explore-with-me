package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validation.NotBlankOrNull;

import jakarta.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventRequest {

    @PositiveOrZero
    private Integer participantLimit;

    @NotBlankOrNull
    @Size(min = 20, max = 7000)
    private String description;

    @NotBlankOrNull
    @Size(min = 3, max = 120)
    private String title;

    @NotBlankOrNull
    @Size(min = 20, max = 2000)
    private String annotation;

    private Integer category;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Boolean requestModeration;
    private String stateAction;
}
