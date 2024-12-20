package ru.practicum.event.mapper;

import ru.practicum.adapter.DateTimeAdapter;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.CreateEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventMapper {

    public static Event createToEvent(CreateEventDto createEventDto, Category category, User initiator) {
        return Event.builder()
                .title(createEventDto.getTitle())
                .annotation(createEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .description(createEventDto.getDescription())
                .eventDate(DateTimeAdapter.stringToLocalDateTime(createEventDto.getEventDate()))
                .initiator(initiator)
                .location(LocationMapper.mapLocation(createEventDto.getLocation()))
                .paid(Optional.ofNullable(createEventDto.getPaid()).orElse(false))
                .participantLimit(Optional.ofNullable(createEventDto.getParticipantLimit()).orElse(0))
                .createdOn(LocalDateTime.now())
                .requestModeration(Optional.ofNullable(createEventDto.getRequestModeration()).orElse(true))
                .state(State.PENDING)
                .build();
    }

    private static Event updateEvent(Event event, UpdateEventRequest updateEvent, Category category, State state, Location location) {
        return event.toBuilder()
                .annotation(Optional.ofNullable(updateEvent.getAnnotation()).orElse(event.getAnnotation()))
                .category(Optional.ofNullable(updateEvent.getCategory()).map(c -> category).orElse(event.getCategory()))
                .description(Optional.ofNullable(updateEvent.getDescription()).orElse(event.getDescription()))
                .eventDate(Optional.ofNullable(updateEvent.getEventDate()).map(DateTimeAdapter::stringToLocalDateTime).orElse(event.getEventDate()))
                .location(Optional.ofNullable(location).orElse(event.getLocation()))
                .paid(Optional.ofNullable(updateEvent.getPaid()).orElse(event.getPaid()))
                .participantLimit(Optional.ofNullable(updateEvent.getParticipantLimit()).orElse(event.getParticipantLimit()))
                .requestModeration(Optional.ofNullable(updateEvent.getRequestModeration()).orElse(event.getRequestModeration()))
                .title(Optional.ofNullable(updateEvent.getTitle()).orElse(event.getTitle()))
                .state(Optional.ofNullable(state).orElse(event.getState()))
                .publishedOn(event.getPublishedOn())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Map<Integer, Long> viewsMap, Map<Integer, List<CommentDto>> comments) {
        long views = (viewsMap != null && viewsMap.containsKey(event.getId())) ? viewsMap.get(event.getId()) : 0L;

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .description(event.getDescription())
                .eventDate(DateTimeAdapter.toString(event.getEventDate()))
                .createdOn(DateTimeAdapter.toString(event.getCreatedOn()))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState().name())
                .views(views)
                .publishedOn(event.getPublishedOn() != null ? DateTimeAdapter.toString(event.getPublishedOn()) : null)
                .comments(comments != null && comments.containsKey(event.getId())
                        ? comments.get(event.getId())
                        : List.of())
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event, Map<Integer, Long> viewsMap,  Map<Integer, List<CommentDto>> comments) {
        long views = (viewsMap != null && viewsMap.containsKey(event.getId())) ? viewsMap.get(event.getId()) : 0L;
        List<CommentDto> listComments = (comments != null && comments.containsKey(event.getId())) ? comments.get(event.getId()) : List.of();

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(DateTimeAdapter.toString(event.getEventDate()))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .comments(listComments)
                .build();
    }

    public static Event adminUpdateEvent(Event event, UpdateEventRequest updateEvent, Category category,
                                         State state, Location location) {
        return updateEvent(event, updateEvent, category, state, location);
    }

    public static Event privateUpdateEvent(Event event, UpdateEventRequest updateEvent, Category category, State state) {
        return updateEvent(event, updateEvent, category, state, null);
    }
}
