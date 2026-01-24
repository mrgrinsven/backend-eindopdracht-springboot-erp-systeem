package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;

public class StockMovementMapper {

    public static StockMovement toEntity(StockMovementOutputDto stockMovementOutputDto) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(stockMovementOutputDto.quantity());
        stockMovement.setType(stockMovementOutputDto.type());

        return stockMovement;
    }

    public static StockMovementOutputDto toWoDto(StockMovement stockMovement) {
        return new StockMovementOutputDto(
                stockMovement.getId(),
                stockMovement.getPart().getId(),
                stockMovement.getPart().getName(),
                stockMovement.getQuantity(),
                stockMovement.getType(),
                stockMovement.getWorkOrder().getId(),
                stockMovement.getOrderType(),
                stockMovement.getCreatedBy(),
                stockMovement.getCreationDate()
        );
    }

    public static StockMovementOutputDto toPoDto(StockMovement stockMovement) {
        return new StockMovementOutputDto(
                stockMovement.getId(),
                stockMovement.getPart().getId(),
                stockMovement.getPart().getName(),
                stockMovement.getQuantity(),
                stockMovement.getType(),
                stockMovement.getPurchaseOrder().getId(),
                stockMovement.getOrderType(),
                stockMovement.getCreatedBy(),
                stockMovement.getCreationDate()
        );
    }
}
