package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;

import java.time.LocalDateTime;

public record StockMovementOutputDto(
        Long id,
        Long partId,
        String partName,
        Integer quantity,
        Integer type,
        Long orderID,
        StockMovement.OrderType orderType,
        String createdBy,
        LocalDateTime date
) {}
