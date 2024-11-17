package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuppressWarnings("checkstyle:Regexp")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElementStatsSaveDto {
    private int id;

    @NotNull
    @NotEmpty
    private String app;

    @NotNull
    @NotEmpty
    private String uri;

    @NotNull
    @NotEmpty
    private String ip;

    @NotEmpty
    @NotNull
    private LocalDateTime createdDate;
}
