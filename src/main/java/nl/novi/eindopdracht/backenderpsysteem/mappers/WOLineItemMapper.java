package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.WOLineItemInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WOLineItemOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.WOLineItem;

public class WOLineItemMapper {
    public static WOLineItem toEntity(WOLineItemInputDto woLineItemInputDto) {
        WOLineItem WOLineItem = new WOLineItem();
        WOLineItem.setQuantity(woLineItemInputDto.quantity());
        return WOLineItem;
    }

    public static WOLineItemOutputDto toDto(WOLineItem WOLineItem) {
        return new WOLineItemOutputDto(
                WOLineItem.getPart().getId(),
                WOLineItem.getPart().getName(),
                WOLineItem.getPart().getPartNumber(),
                WOLineItem.getQuantity()
        );
    }
}
