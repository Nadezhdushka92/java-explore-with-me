package ru.practicum.event.dto;


import jakarta.persistence.Transient;
import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.*;


@SuppressWarnings("checkstyle:Regexp")
@Data
@EqualsAndHashCode(of = "id")
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

    @Transient
    private Integer confirmedRequests;

    @Size(min = 20, max = 7000)
    private String description;

    @Min(value = 0)
    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    @NotNull
    private String state;

    @Transient
    private Long views;
}
