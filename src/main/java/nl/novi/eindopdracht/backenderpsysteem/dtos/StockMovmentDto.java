package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDate;

public record StockMovmentDto(
        Long id,
        Long partId,
        String partName,
        Integer quantity,
        Integer type,
        LocalDate date,
        Long orderID,
        String orderType
) {}
