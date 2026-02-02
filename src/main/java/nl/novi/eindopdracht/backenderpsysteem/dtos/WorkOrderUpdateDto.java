package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record WorkOrderUpdateDto(
        @NotNull
        @Positive
        Long equipmentId,
        @NotNull
        @Positive
        Integer repairTime,
        @NotNull
        List<WOLineItemUpdateDto> items
) {}
