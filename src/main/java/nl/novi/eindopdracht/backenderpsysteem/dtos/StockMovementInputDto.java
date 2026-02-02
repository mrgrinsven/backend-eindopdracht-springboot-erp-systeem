package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockMovementInputDto(
        @NotNull
        @Positive
        Long lineItemId,
        @NotNull
        @Positive
        Integer quantity
) {}
