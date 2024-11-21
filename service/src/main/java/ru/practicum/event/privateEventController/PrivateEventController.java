package ru.practicum.event.privateEventController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService privateEventService;

    @GetMapping
    public List<EventShortDto> getListEvents(@PathVariable int userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return privateEventService.getEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Valid @RequestBody CreateEventDto createEventDto) {
        return privateEventService.createEvent(userId, createEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable int userId, @PathVariable int eventId) {
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int userId, @PathVariable int eventId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return privateEventService.updateEvent(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable int userId, @PathVariable int eventId) {
        return privateEventService.getRequestForEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestForEvent(@PathVariable int userId,
                                                                      @PathVariable int eventId,
                                                                      @RequestBody @Valid EventRequestStatusUpdateRequest
                                                                              statusUpdateRequest) {
        return privateEventService.updateStatusRequestForEvent(userId, eventId, statusUpdateRequest);
    }
}
