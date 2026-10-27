package org.example.housematesolution.Presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateHouseRequest(
        @NotBlank @Size(max = 50) String houseName,
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Email @Size(max = 100) String email
) {
}
