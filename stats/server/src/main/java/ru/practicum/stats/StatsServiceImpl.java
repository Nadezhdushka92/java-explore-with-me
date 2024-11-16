package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.adapter.DateTimeAdapter;
import ru.practicum.stats.exception.DataNotValidException;
import ru.practicum.stats.mapper.ElementStatsMapper;
import ru.practicum.stats.model.Stats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @SuppressWarnings("checkstyle:Regexp")
    @Override
    public List<ElementStatsResponseDto> getStatsFromService(String start, String end, List<String> uris, boolean unique) {

        LocalDateTime startTime = DateTimeAdapter.stringToLocalDateTime(
                URLDecoder.decode(start, StandardCharsets.UTF_8));
        LocalDateTime endTime = DateTimeAdapter.stringToLocalDateTime(
                URLDecoder.decode(end, StandardCharsets.UTF_8));

        if (startTime.isAfter(endTime)) {
            throw new DataNotValidException("Incorrect Time");
        }

        List<Stats> stats = fetchStats(startTime, endTime, uris, unique);
        return mapToResponseDto(stats);
    }

    //Метод для преобразования ответа в ElementStatsResponseDto
    private List<ElementStatsResponseDto> mapToResponseDto(List<Stats> stats) {
        // Подсчитываем статистику по URI
        Map<String, Long> statsCount = countStatsByUri(stats);

        return statsCount.entrySet().stream()
                .map(entry -> createResponseDto(entry.getKey(), entry.getValue(), stats))
                .filter(Objects::nonNull) // Фильтруем null значения
                .sorted(Comparator.comparingLong(ElementStatsResponseDto::getHits).reversed()) // Сортируем по хитов
                .collect(Collectors.toList()); // Собираем в список
    }

    //Метод для преобразования каждого отдельного элемента в ElementStatsResponseDto
    private ElementStatsResponseDto createResponseDto(String uri, Long count, List<Stats> stats) {
        Stats elementStats = stats.stream()
                .filter(e -> e.getUri().equals(uri))
                .findFirst()
                .orElse(null);

        if (elementStats == null) return null;

        return ElementStatsMapper.mapToStatsDto(elementStats, count);
    }

    //получаем мапу из URI и количесва просмотров этого URI
    private Map<String, Long> countStatsByUri(List<Stats> stats) {
        return stats.stream()
                .collect(Collectors.groupingBy(Stats::getUri, Collectors.counting()));
    }

    //Получение статистики из БД
    private List<Stats> fetchStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, boolean unique) {
            boolean hasUris = (uris != null && !uris.isEmpty());

            if (unique) {
                if (hasUris) {
                    return statsRepository.getStatsOriginalIp(startTime, endTime, uris);
                } else {
                    return statsRepository.getStatsOriginalIp(startTime, endTime);
                }
            } else {
                if (hasUris) {
                    return statsRepository.getStatsNotOriginalIp(startTime, endTime, uris);
                } else {
                    return statsRepository.getStatsNotOriginalIp(startTime, endTime);
                }
            }
    }

    @Override
    public void saveHit(ElementStatsSaveDto statsSaveDto) {
        statsSaveDto.setCreatedDate(LocalDateTime.now());
        Stats newElementStats = ElementStatsMapper.mapToStats(statsSaveDto);
        statsRepository.save(newElementStats);
        log.info("Хит сохранен в БД");
    }
}