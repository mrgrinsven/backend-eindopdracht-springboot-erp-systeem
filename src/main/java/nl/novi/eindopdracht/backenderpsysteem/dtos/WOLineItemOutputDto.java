package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.WOLineItem;

public record WOLineItemOutputDto(
        Long id,
        Long partId,
        String partName,
        String partNumber,
        Integer quantity,
        WOLineItem.Status status
) {}
