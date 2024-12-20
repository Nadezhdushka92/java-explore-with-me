package ru.practicum.event.adminEventController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getListAdminEvents(@RequestParam(required = false) List<Integer> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getListAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchAdminEvent(@PathVariable int eventId,
                                        @RequestBody @Valid UpdateEventRequest updateEvent) {
        return eventService.patchAdminEvent(eventId, updateEvent);
    }
}
