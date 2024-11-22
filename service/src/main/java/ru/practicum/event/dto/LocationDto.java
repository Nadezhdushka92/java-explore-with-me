package ru.practicum.event.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LocationDto {
    private double lat;
    private double lon;
}
