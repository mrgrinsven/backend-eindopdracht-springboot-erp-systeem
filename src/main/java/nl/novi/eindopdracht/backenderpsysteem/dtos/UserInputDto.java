package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserInputDto(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,
        @NotEmpty(message = "At least one role must be assigned")
        String[] roles,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String businessEmail
) {}
