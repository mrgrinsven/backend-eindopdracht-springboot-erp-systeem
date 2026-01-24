package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record WOLineItemOutputDto(
        Long partId,
        String partName,
        String partNumber,
        int quantity
) {}
