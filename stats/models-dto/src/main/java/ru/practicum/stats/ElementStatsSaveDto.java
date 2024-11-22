package ru.practicum.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
