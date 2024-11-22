package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequest(int userId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int eventId);
}
