package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrderLineItem;

import java.time.LocalDate;

public record PurchaseOrderLineItemDto(
        PartDto partDto,
        int quantity,
        double unitPrice,
        PurchaseOrderLineItem.DeliveryStatus deliveryStatus,
        LocalDate deliveryDate
) {}
