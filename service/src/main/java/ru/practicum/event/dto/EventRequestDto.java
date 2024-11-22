package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventRequestDto {
    private int id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    private String eventDate;
    private UserShortDto initiator;
    private String description;
    private Integer participantLimit;
    private String state;
    private String createdOn;
    private LocationDto location;
    private Boolean requestModeration;
}
