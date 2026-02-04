package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDto(
        @NotBlank
        String currentPassword,
        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {}
