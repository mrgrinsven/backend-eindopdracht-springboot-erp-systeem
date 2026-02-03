package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record POLineItemUpdateDto(
        @Positive
        Long id,
        @NotNull
        @Positive
        Long partId,
        @NotNull
        @Positive
        int quantity,
        @NotNull
        @PositiveOrZero
        double unitPrice,
        @NotNull
        @FutureOrPresent
        LocalDate deliveryDate
) {}
