package ru.practicum.request.mapper;

import ru.practicum.adapter.DateTimeAdapter;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {

    public static ParticipationRequestDto mapToParticipationRequestDto(Request request) {

        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateTimeAdapter.toString(request.getCreatedTime()))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().name())
                .build();
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDtoList(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::mapToParticipationRequestDto)
                .toList();
    }

    public static Request createRequest(User requester, Event event) {

        return Request.builder()
                .createdTime(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();
    }
}
