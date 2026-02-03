package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotBlank;

public record EquipmentInputDto(
        @NotBlank
        String name
) {}
