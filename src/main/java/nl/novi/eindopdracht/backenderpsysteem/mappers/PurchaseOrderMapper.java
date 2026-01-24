package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrderLineItem;

import java.util.List;

public class PurchaseOrderMapper {

    public static PurchaseOrder toEntity(PurchaseOrderInputDto purchaseOrderInputDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setVendorName(purchaseOrderInputDto.vendorName());
        List<PurchaseOrderLineItem> items = purchaseOrderInputDto
                .items()
                .stream()
                .map(PurchaseOrderLineItemMapper::toEntity)
                .toList();

        return purchaseOrder;
    }

    public static PurchaseOrderDto toDto(PurchaseOrder purchaseOrder) {
        return new PurchaseOrderDto(
                purchaseOrder.getId(),
                purchaseOrder.getVendorName(),
                purchaseOrder.getTotalPrice(),
                purchaseOrder.getOrderStatus(),
                purchaseOrder.getItems()
                        .stream()
                        .map(PurchaseOrderLineItemMapper::toDto)
                        .toList(),
                purchaseOrder.getCreationDate(),
                purchaseOrder.getCreatedBy(),
                purchaseOrder.getModifiedBy()
        );
    }
}
