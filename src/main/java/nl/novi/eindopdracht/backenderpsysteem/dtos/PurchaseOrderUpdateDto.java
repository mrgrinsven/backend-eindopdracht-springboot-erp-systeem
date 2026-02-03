package nl.novi.eindopdracht.backenderpsysteem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PurchaseOrderUpdateDto(
        @NotBlank
        String vendorName,
        @NotNull
        List<POLineItemUpdateDto> items
) {}
