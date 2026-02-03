package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PartInputDto(
        @NotBlank
        String name,
        @NotBlank
        String partNumber,
        @NotNull
        @PositiveOrZero
        Double unitPrice,
        @NotNull
        @PositiveOrZero
        Integer reorderPoint,
        @NotNull
        @PositiveOrZero
        Integer reorderQuantity
) {}
