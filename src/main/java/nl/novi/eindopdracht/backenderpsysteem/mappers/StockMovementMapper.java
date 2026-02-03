package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;

public class StockMovementMapper {

    public static StockMovement toEntity(StockMovementInputDto stockMovementInputDto) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(stockMovementInputDto.quantity());

        return stockMovement;
    }

    public static StockMovementOutputDto toDto(StockMovement stockMovement) {
        Long orderId = switch (stockMovement.getStockMovementType().getOrderType()) {
            case "PURCHASE_ORDER" -> stockMovement.getPurchaseOrder().getId();
            case "WORK_ORDER" -> stockMovement.getWorkOrder().getId();
            default -> null;
        };

        return new StockMovementOutputDto(
                stockMovement.getId(),
                stockMovement.getPart().getId(),
                stockMovement.getPart().getName(),
                stockMovement.getQuantity(),
                stockMovement.getStockMovementType().getStockMovementCode(),
                stockMovement.getStockMovementType().getDescription(),
                orderId,
                stockMovement.getStockMovementType().getOrderType(),
                stockMovement.getCreatedBy(),
                stockMovement.getCreationDate()
        );
    }
}
