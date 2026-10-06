package org.example.housematesolution.Presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record JoinHouseRequest(
        @NotBlank String joinCode,
        @NotBlank String name,
        @NotBlank @Email String email
) {
}
