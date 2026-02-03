package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;

public class PurchaseOrderMapper {

    public static PurchaseOrder toEntity(PurchaseOrderInputDto purchaseOrderInputDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setVendorName(purchaseOrderInputDto.vendorName());

        return purchaseOrder;
    }

    public static PurchaseOrderOutputDto toDto(PurchaseOrder purchaseOrder) {
        return new PurchaseOrderOutputDto(
                purchaseOrder.getId(),
                purchaseOrder.getVendorName(),
                purchaseOrder.getTotalPrice(),
                purchaseOrder.getIsOpen(),
                purchaseOrder.getCreationDate(),
                purchaseOrder.getCreatedBy(),
                purchaseOrder.getModifiedBy(),
                purchaseOrder.getItems()
                        .stream()
                        .map(POLineItemMapper::toDto)
                        .toList()
        );
    }
}
