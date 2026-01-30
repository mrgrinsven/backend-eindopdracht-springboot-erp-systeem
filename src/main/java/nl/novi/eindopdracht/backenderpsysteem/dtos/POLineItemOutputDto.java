package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;

import java.time.LocalDate;

public record POLineItemOutputDto(
        Long id,
        Long partId,
        String partName,
        String partNumber,
        Integer quantity,
        Double unitPrice,
        LocalDate deliveryDate,
        POLineItem.DeliveryStatus deliveryStatus
) {}
