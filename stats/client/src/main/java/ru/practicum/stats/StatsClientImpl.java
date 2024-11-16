package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.adapter.DateTimeAdapter;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatsClientImpl implements StatsClient {
    private final RestTemplate restTemplate;

    private final String serverUri = "http://stats-server:9090";

    @Override
    public List<ElementStatsResponseDto> getStats(LocalDateTime start,
                                                  LocalDateTime end,
                                                  List<String> uris,
                                                  Boolean unique) {

        if (start == null) {
            start = LocalDateTime.of(2000, 2, 1, 12, 0);
        }
        if (end == null) {
            end = LocalDateTime.of(2047, 2, 1, 12, 0);
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serverUri + "/stats");

        uriBuilder.queryParam("start", codingTime(start));

        uriBuilder.queryParam("end", codingTime(end));

        if (uris != null && !uris.isEmpty()) {
            String uriString = String.join(",", uris);
            uriBuilder.queryParam("uris", uriString);
        }

        if (unique != null) {
            uriBuilder.queryParam("unique", unique);
        }

        String finalUrl = uriBuilder.toUriString();

        try {
            ResponseEntity<List<ElementStatsResponseDto>> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении статистики: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private String codingTime(LocalDateTime time) {
        return DateTimeAdapter.toString(time);
    }

    @Override
    public void saveHit(String app,
                        String uri,
                        String ip,
                        LocalDateTime createDate) {
        String url = serverUri + "/hit";

        ElementStatsSaveDto statsSaveDto = ElementStatsSaveDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .createdDate(createDate)
                .build();
        HttpEntity<ElementStatsSaveDto> entity = createHttpEntity(statsSaveDto);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Хит успешно сохранен.");
            } else {
                System.out.println("Не удалось сохранить хит: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении хита: " + e.getMessage());
        }
    }

    private HttpEntity<ElementStatsSaveDto> createHttpEntity(ElementStatsSaveDto statsSaveDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(statsSaveDto, headers);
    }
}
