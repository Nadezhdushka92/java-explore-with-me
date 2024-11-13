package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
public class StatsClientImpl implements StatsClient {
    private final RestTemplate restTemplate;

    @Value("${stats-server.url}")
    private String serverUri;

    @Override
    public List<ElementStatsResponseDto> getStats(@NotNull @NotEmpty String start,
                                                  @NotNull @NotEmpty String end,
                                                  List<String> uris, Boolean unique) {

        StringBuilder url = new StringBuilder(serverUri + "/stats?start=" +
                                              codingTime(start) + "&end=" + codingTime(end));

        if (uris != null && !uris.isEmpty()) {
            url.append("&uris=").append(String.join(",", uris));
        }
        if (unique != null) {
            url.append("&unique=").append(unique);
        }

        try {
            ResponseEntity<List<ElementStatsResponseDto>> response = restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Ошибка при получении статистики: " + e.getMessage());
            return List.of();
        }
    }

    private String codingTime(String time) {
        return URLEncoder.encode(time, StandardCharsets.UTF_8);

    }

    @Override
    public void saveHit(@NotNull @NotEmpty String app,
                        @NotNull @NotEmpty String uri,
                        @NotNull @NotEmpty String ip,
                        @NotNull LocalDateTime createDate) {
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
