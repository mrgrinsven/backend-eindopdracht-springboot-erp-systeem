package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDateTime;

public record StockMovementDto(
        Long id,
        Long partId,
        String partName,
        Integer quantity,
        Integer type,
        LocalDateTime date,
        Long orderID,
        String orderType,
        String createdBy
) {}
