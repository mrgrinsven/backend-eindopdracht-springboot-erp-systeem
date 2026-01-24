package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.POLineItemOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrderLineItem;

public class PurchaseOrderLineItemMapper {

    public static PurchaseOrderLineItem toEntity(POLineItemOutputDto POLineItemOutputDto) {
        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem();
        purchaseOrderLineItem.setQuantity(POLineItemOutputDto.quantity());
        purchaseOrderLineItem.setUnitPrice(POLineItemOutputDto.unitPrice());
        purchaseOrderLineItem.setDeliveryDate(POLineItemOutputDto.deliveryDate());

        return purchaseOrderLineItem;
    }

    public static POLineItemOutputDto toDto(PurchaseOrderLineItem purchaseOrderLineItem) {
        return new POLineItemOutputDto(
                purchaseOrderLineItem.getPart().getId(),
                purchaseOrderLineItem.getPart().getName(),
                purchaseOrderLineItem.getPart().getPartNumber(),
                purchaseOrderLineItem.getQuantity(),
                purchaseOrderLineItem.getUnitPrice(),
                purchaseOrderLineItem.getDeliveryDate(),
                purchaseOrderLineItem.getDeliveryStatus()
        );
    }
}
