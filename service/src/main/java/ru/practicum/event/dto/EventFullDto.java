package ru.practicum.event.dto;


import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import jakarta.validation.constraints.*;


@SuppressWarnings("checkstyle:Regexp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventFullDto {
    @Size(min = 20, max = 2000)
    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    private Boolean paid;

    @Size(min = 3, max = 120)
    @NotBlank
    @NotNull
    private String title;

    @NotNull
    private String createdOn;

    private Integer id;

    private Integer confirmedRequests;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @PositiveOrZero
    @Min(value = 0)
    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    @NotNull
    private String state;

    private Long views;
}
