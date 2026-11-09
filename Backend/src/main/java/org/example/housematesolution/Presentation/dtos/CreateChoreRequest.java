package org.example.housematesolution.Presentation.dtos;

import jakarta.validation.constraints.*;

public record CreateChoreRequest(
        @NotBlank @Size(max = 50) String name,
        @Min(1) @Max(7) int intervalDays,
        @Min(1) @Max(3) int effort
) {
}
