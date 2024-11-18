package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.request.dto.ParticipationRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface EventService {

    List<EventShortDto> getEvents(int userId, int from, int size);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto getEvent(int userId, int eventId);

    EventFullDto updateEvent(int userId, int eventId, @NotNull @Valid UpdateEventRequest updateEventRequest);

    List<ParticipationRequestDto> getRequestForEvent(int userId, int eventId);

    EventRequestStatusUpdateResult updateStatusRequestForEvent(int userId, int eventId,
                                                               EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventFullDto> getAdminEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart,
                                      String rangeEnd, int from, int size);

    EventFullDto patchAdminEvent(int eventId,@NotNull @Valid UpdateEventRequest updateEvent);

    List<EventShortDto> getPublicEvents(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventFullDto getPublicEvent(int id, HttpServletRequest request);
}
