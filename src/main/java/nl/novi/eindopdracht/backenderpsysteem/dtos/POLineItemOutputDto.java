package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrderLineItem;

import java.time.LocalDate;

public record POLineItemOutputDto(
        Long partId,
        String partName,
        String partNumber,
        int quantity,
        double unitPrice,
        LocalDate deliveryDate,
        PurchaseOrderLineItem.DeliveryStatus deliveryStatus
) {}
