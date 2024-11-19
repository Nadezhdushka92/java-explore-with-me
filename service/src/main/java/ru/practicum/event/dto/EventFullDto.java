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
    @NotEmpty
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    @NotEmpty
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    @NotEmpty
    private LocationDto location;

    private Boolean paid;

    @Size(min = 3, max = 120)
    @NotBlank
    private String title;

    @NotNull
    private String createdOn;

    private Integer id;

    private Integer confirmedRequests;

    @Size(min = 20, max = 7000)
    private String description;

    @PositiveOrZero
    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    @NotNull
    private String state;

    private Long views;
}
