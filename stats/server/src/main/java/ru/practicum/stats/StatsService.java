package ru.practicum.stats;

import ru.practicum.stats.ElementStatsResponseDto;
import ru.practicum.stats.ElementStatsSaveDto;

import java.util.List;

public interface StatsService {
    List<ElementStatsResponseDto> getStatsFromService(String start, String end, List<String> uris, boolean unique);

    void saveHit(ElementStatsSaveDto statsSaveDto);
}
