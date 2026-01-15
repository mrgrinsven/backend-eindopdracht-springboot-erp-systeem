package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDate;

public record PurchaseOrderLineItemDto(
        Long partId,
        String partName,
        int quantity,
        double unitPrice,
        String deliveryStatus,
        LocalDate deliveryDate
) {}
