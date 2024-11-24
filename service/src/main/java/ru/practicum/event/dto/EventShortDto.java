package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventShortDto {
    private int id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
    private List<CommentDto> comments;
}