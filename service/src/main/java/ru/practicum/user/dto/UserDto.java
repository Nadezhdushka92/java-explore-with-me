package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Positive
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotEmpty
    @NotNull
    @NotBlank
    private String email;
}
