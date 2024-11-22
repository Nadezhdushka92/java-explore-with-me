package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByIdIn(List<Integer> ids);

    List<Request> findByEventId(int eventId);

    List<Request> findByRequesterId(int requesterId);

    boolean existsByRequesterIdAndEventId(int requesterId, int eventId);
}
