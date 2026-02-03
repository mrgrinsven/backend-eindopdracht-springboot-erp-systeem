package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WOLineItemUpdateDto(
        @Positive
        Long id,
        @NotNull
        @Positive
        Long partId,
        @NotNull
        @Positive
        int quantity
) {}
