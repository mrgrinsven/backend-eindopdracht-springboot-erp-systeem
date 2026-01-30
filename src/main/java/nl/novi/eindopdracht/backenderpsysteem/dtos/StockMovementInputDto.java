package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record StockMovementInputDto(
        Long lineItemId,
        Long partId,
        Integer quantity
) {}
