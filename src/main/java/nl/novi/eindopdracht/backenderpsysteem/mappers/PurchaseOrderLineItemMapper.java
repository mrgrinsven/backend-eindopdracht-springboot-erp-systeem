package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderLineItemDto;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrderLineItem;

public class PurchaseOrderLineItemMapper {

    public static PurchaseOrderLineItem toEntity(PurchaseOrderLineItemDto purchaseOrderLineItemDto) {
        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem();
        purchaseOrderLineItem.setQuantity(purchaseOrderLineItemDto.quantity());
        purchaseOrderLineItem.setUnitPrice(purchaseOrderLineItemDto.unitPrice());
        purchaseOrderLineItem.setDeliveryDate(purchaseOrderLineItemDto.deliveryDate());

        return purchaseOrderLineItem;
    }

    public static PurchaseOrderLineItemDto toDto(PurchaseOrderLineItem purchaseOrderLineItem) {
        return new PurchaseOrderLineItemDto(
                PartMapper.toDto(purchaseOrderLineItem.getPart()),
                purchaseOrderLineItem.getQuantity(),
                purchaseOrderLineItem.getUnitPrice(),
                purchaseOrderLineItem.getDeliveryStatus(),
                purchaseOrderLineItem.getDeliveryDate()
        );
    }
}
