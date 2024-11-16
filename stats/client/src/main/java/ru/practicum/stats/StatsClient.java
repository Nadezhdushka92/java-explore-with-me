package ru.practicum.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {

    List<ElementStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void saveHit(String app, String uri, String ip, LocalDateTime createdDate);

}