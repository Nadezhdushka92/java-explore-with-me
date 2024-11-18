package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.adapter.DateTimeAdapter;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.LocationMapper;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stats.ElementStatsResponseDto;
import ru.practicum.stats.StatsClient;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.practicum.event.model.QEvent.event;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final String thisService = "ewm-main-service";
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventShortDto> getEvents(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);

        log.info("Получение страницы ивентов");
        List<Event> categoryPage = eventRepository.findByInitiatorId(userId, pageable);
        Map<Integer, Long> viewsMap = getViews(categoryPage);

        log.info("Выполнение метода getEvents");
        return categoryPage.stream()
                .map(event -> EventMapper.mapToEventShortDto(event, viewsMap))
                .toList();
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found", ""));

        if (isCheckinEventTime(newEventDto.getEventDate())) {
            throw new ValidationException("Incorrect data", "");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("Category not found", ""));

        locationRepository.save(LocationMapper.mapLocation(newEventDto.getLocation()));

        Event newEvent = EventMapper.createToEvent(newEventDto, category, initiator);
        Event createEvent = eventRepository.save(newEvent);

        Map<Integer, Long> viewsMap = getViews(List.of(createEvent));

        return EventMapper.toEventFullDto(createEvent, viewsMap);
    }

    @Override
    public EventFullDto getEvent(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found", ""));

        Map<Integer, Long> viewsMap = getViews(List.of(event));

        return EventMapper.toEventFullDto(event, viewsMap);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(int userId, int eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found", ""));

        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Event have PUBLISHED state", "");
        }

        if (updateEventRequest.getEventDate() != null && isCheckinEventTime(updateEventRequest.getEventDate())) {
            throw new ValidationException("Incorrect Time", "");
        }

        Category category = null;
        if (updateEventRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() ->
                    new NotFoundException("Category not found", ""));
        }

        State state = null;
        if (updateEventRequest.getStateAction() != null) {
            state = updateEventRequest.getStateAction().equalsIgnoreCase("CANCEL_REVIEW") ? State.CANCELED : State.PENDING;
        }

        Event updatedEvent = EventMapper.privateUpdateEvent(event, updateEventRequest, category, state);
        Event savedEvent = eventRepository.save(updatedEvent);

        Map<Integer, Long> viewsMap = getViews(List.of(savedEvent));

        return EventMapper.toEventFullDto(savedEvent, viewsMap);
    }

    @Override
    public List<ParticipationRequestDto> getRequestForEvent(int userId, int eventId) {
        List<Request> request = requestRepository.findByEventId(eventId);

        return request.stream()
                .map(RequestMapper::mapToParticipationRequestDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequestForEvent(int userId, int eventId,
                                                                      EventRequestStatusUpdateRequest
                                                                              statusUpdateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено", ""));

        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();

        if (participantLimit > 0 && participantLimit <= confirmedRequests) {
            throw new ConflictException("Больше создать заявок нельзя", "");
        }

        RequestStatus requestStatus = RequestStatus.valueOf(statusUpdateRequest.getStatus());
        List<Request> requests = requestRepository.findByIdIn(statusUpdateRequest.getRequestIds());
        List<Request> updatedRequests = new ArrayList<>();
        List<Request> noUpdateRequests = new ArrayList<>();

        for (Request request : requests) {
            log.info("Если заявки отменяются");
            if (requestStatus != RequestStatus.CONFIRMED) {
                if (request.getStatus() != RequestStatus.PENDING) {
                    throw new ConflictException("Заявка не в состоянии ожидания", "");
                }
                request.setStatus(requestStatus);
                noUpdateRequests.add(request);

            } else {
                log.info("Заявки одобряются");

                if (participantLimit > confirmedRequests) {
                    log.info("Проверяем лимит участников");
                    if (request.getStatus() != RequestStatus.PENDING) {
                        throw new ConflictException("Заявка не в состоянии ожидания", "");
                    }
                    request.setStatus(requestStatus);
                    confirmedRequests++;
                    updatedRequests.add(request);
                } else {
                    if (request.getStatus() != RequestStatus.PENDING) {
                        throw new ConflictException("Заявка не в состоянии ожидания", "");
                    }
                    request.setStatus(requestStatus);
                    noUpdateRequests.add(request);
                }
            }
        }

        requestRepository.saveAll(updatedRequests);
        requestRepository.saveAll(noUpdateRequests);
        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(!updatedRequests.isEmpty()
                        ? RequestMapper.mapToParticipationRequestDtoList(updatedRequests)
                        : List.of())
                .rejectedRequests(!noUpdateRequests.isEmpty()
                        ? RequestMapper.mapToParticipationRequestDtoList(noUpdateRequests)
                        : List.of())
                .build();
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart, String rangeEnd, int from, int size) {
        QEvent event = QEvent.event;

        BooleanExpression predicate = event.isNotNull();
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);

        if (users != null && !users.isEmpty()) {
            predicate = predicate.and(event.initiator.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            List<State> stateList = states.stream()
                    .map(State::valueOf)
                    .toList();
            predicate = predicate.and(event.state.in(stateList));
        }

        if (categories != null && !categories.isEmpty()) {
            predicate = predicate.and(event.category.id.in(categories));
        }

        predicate = addDateRangePredicate(predicate, rangeStart, rangeEnd);

        List<Event> events = queryFactory.selectFrom(event)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Integer, Long> viewsMap = getViews(events);
        return events.stream()
                .map(eventStream -> EventMapper.toEventFullDto(eventStream, viewsMap))
                .toList();

    }

    @SuppressWarnings("checkstyle:Regexp")
    @Override
    @Transactional
    public EventFullDto patchAdminEvent(int eventId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено", ""));

        validateEventDate(updateEvent, event);
        State state = determineState(updateEvent, event);

        Category category = (updateEvent.getCategory() != null)
                ? categoryRepository.findById(updateEvent.getCategory()).orElseThrow(() ->
                new NotFoundException("Категории нет", ""))
                : null;

        Location location = (updateEvent.getLocation() != null)
                ? locationRepository.save(LocationMapper.mapLocation(updateEvent.getLocation()))
                : null;

        Event updatedEvent = EventMapper.adminUpdateEvent(event, updateEvent, category, state, location);
        Event newEvent = eventRepository.save(updatedEvent);
        Map<Integer, Long> viewsMap = getViews(List.of(newEvent));

        return EventMapper.toEventFullDto(newEvent, viewsMap);
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {

        QEvent event = QEvent.event;
        BooleanExpression predicate = event.isNotNull();

        if (text != null && !text.isEmpty()) {
            predicate = predicate.and(event.annotation.containsIgnoreCase(text)
                    .or(event.description.containsIgnoreCase(text)));
        }

        if (categories != null && !categories.isEmpty()) {
            predicate = predicate.and(event.category.id.in(categories));
        }

        if (paid != null) {
            predicate = predicate.and(event.paid.eq(paid));
        }

        predicate = addDateRangePredicate(predicate, rangeStart, rangeEnd);

        if (onlyAvailable != null && onlyAvailable) {
            predicate = predicate.and(event.participantLimit.gt(event.confirmedRequests));
        }

        // Обработка пагинации
        int pageNumber = from > 0 ? from / size : 0;
        size = size > 0 ? size : 10; // Значение по умолчанию, если size равно 0
        Pageable pageable = PageRequest.of(pageNumber, size);

        List<Event> events = queryFactory.select(event)
                .from(event)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Integer, Long> viewsMpa = getViews(events);

        // Сохранение статистики
        statsClient.saveHit(thisService, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        List<EventShortDto> eventShortDto = events.stream()
                .map(eventStream -> EventMapper.mapToEventShortDto(eventStream, viewsMpa))
                .toList();

        // Сортировка событий
        List<EventShortDto> eventsSort;
        if ("EVENT_DATE".equals(sort)) {
            eventsSort = eventShortDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .toList();
        } else {
            eventsSort = eventShortDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                    .toList();
        }

        log.info("Found: {} events", eventsSort.size());

        return eventsSort;
    }

    @Override
    public EventFullDto getPublicEvent(int id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("События нет", ""));

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Событие не опубликованно", "");
        }

        Map<Integer, Long> viewsMpa = getViews(List.of(event));

        statsClient.saveHit(thisService, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return EventMapper.toEventFullDto(event, viewsMpa);
    }

    private Map<Integer, Long> getViews(List<Event> events) {
        List<String> eventId = events.stream()
                .map(Event::getId)
                .map(id -> "/events/" + id)
                .toList();
        log.info("eventId {}", eventId);

        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        List<ElementStatsResponseDto> stats = statsClient.getStats(start, LocalDateTime.now().plusHours(1), eventId, true);

        log.info("Stats: {}", stats.toString());
        return stats.stream()
                .filter(stat -> stat.getUri() != null)
                .map(stat -> {
                    String uri = stat.getUri();
                    log.info("uri {}", uri);

                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(uri);
                    int id = 0;
                    if (matcher.find()) {
                        id = Integer.parseInt(matcher.group());
                    }

                    return Map.entry(id, stat.getHits());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    private boolean isCheckinEventTime(String eventDate) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusHours(2);
        LocalDateTime date = DateTimeAdapter.stringToLocalDateTime(eventDate);
        return date.isBefore(currentDateTime);
    }

    private void validateEventDate(UpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getEventDate() != null) {
            LocalDateTime eventDate = DateTimeAdapter.stringToLocalDateTime(updateEvent.getEventDate());
            log.info("eventDate {}", eventDate);
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата не должна быть раньше нынешнего времени", "");
            }
        }
    }

    private State determineState(UpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updateEvent.getStateAction());
            log.info("Обработка запроса на изменение состояния события: {}", stateAction);

            if (stateAction == StateAction.PUBLISH_EVENT) {
                if (event.getState() != State.PENDING) {
                    log.warn("Попытка опубликовать событие, которое не находится в состоянии ожидания: {}", event.getId());
                    throw new ConflictException("Событие не в ожидании публикации", "");
                }
                return State.PUBLISHED;

            } else if (stateAction == StateAction.REJECT_EVENT) {
                if (event.getState() == State.PUBLISHED) {
                    log.warn("Попытка отклонить уже опубликованное событие: {}", event.getId());
                    throw new ConflictException("Событие уже опубликованно", "");
                }
                return State.CANCELED;
            }
        }

        return null;
    }

    private BooleanExpression addDateRangePredicate(BooleanExpression predicate, String rangeStart, String rangeEnd) {
        LocalDateTime startDate = null;
        if (rangeStart != null) {
            startDate = DateTimeAdapter.stringToLocalDateTime(rangeStart);
            predicate = predicate.and(event.eventDate.goe(startDate));
        }

        LocalDateTime endDate = null;
        if (rangeEnd != null) {
            endDate = DateTimeAdapter.stringToLocalDateTime(rangeEnd);
            predicate = predicate.and(event.eventDate.loe(endDate));
        }

        if ((startDate != null && endDate != null) && startDate.isAfter(endDate)) {
            throw new ValidationException("Неверная дата", "");
        }

        return predicate;
    }
}
