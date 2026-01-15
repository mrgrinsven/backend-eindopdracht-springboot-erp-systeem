package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record WorkOrderLineItemDto(
        Long partId,
        String partName,
        int quantity
) {}
