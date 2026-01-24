package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementDto;
import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;

public class StockMovementMapper {

    public static StockMovement toEntity(StockMovementDto stockMovementDto) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(stockMovementDto.quantity());
        stockMovement.setType(stockMovementDto.type());

        return stockMovement;
    }

    public static StockMovementDto toDto(StockMovement stockMovement) {
        return new StockMovementDto(
                stockMovement.getId(),
                stockMovement.getPart().getId(),
                stockMovement.getPart().getName(),
                stockMovement.getQuantity(),
                stockMovement.getType(),
                stockMovement.getCreationDate(),
                stockMovement.getOrderId(),
                stockMovement.getOrderType(),
                stockMovement.getCreatedBy()
        );
    }
}
