package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDateTime;

public record StockMovementOutputDto(
        Long id,
        Long partId,
        String partName,
        Integer quantity,
        Integer stockMovementCode,
        String movementDescription,
        Long orderID,
        String orderType,
        String createdBy,
        LocalDateTime date
) {}
