package ru.practicum.comment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateCommentDto {
    @Positive
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 6)
    private String text;
}
