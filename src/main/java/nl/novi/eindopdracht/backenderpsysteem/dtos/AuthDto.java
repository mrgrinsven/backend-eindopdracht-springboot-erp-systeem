package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthDto(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Username is required")
        String password
) {}
