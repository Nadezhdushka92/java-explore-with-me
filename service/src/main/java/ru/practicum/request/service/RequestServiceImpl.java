package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getRequest(int userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found", ""));
        List<Request> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(RequestMapper::mapToParticipationRequestDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found", ""));

        requestRepository.findByRequesterId(userId);

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Request nave already", "");
        }

        if (event.getInitiator().getId() == (userId)) {
            throw new ConflictException("Нельзя сделать запрос на свой ивент", "");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event not published", "");
        }

        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        if (participantLimit > 0 && participantLimit <= confirmedRequests) {
            throw new ConflictException("Больше создать заявок нельзя", "");
        }

        User requester = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found", ""));

        Request newRequest = RequestMapper.createRequest(requester, event);

        log.info(event.getRequestModeration().toString());
        if (participantLimit == 0 || !event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
            int confirmedRequestsEvent = event.getConfirmedRequests() + 1;
            event.setConfirmedRequests(confirmedRequestsEvent);
            eventRepository.save(event);
        }

        Request saveRequest = requestRepository.save(newRequest);
        log.info("Saved request userId:{}, eventId:{}",
                saveRequest.getRequester().getId(), saveRequest.getEvent().getId());

        return RequestMapper.mapToParticipationRequestDto(saveRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request not found", ""));

        request.setStatus(RequestStatus.CANCELED);

        Request newRequest = requestRepository.save(request);

        return RequestMapper.mapToParticipationRequestDto(newRequest);
    }
}
