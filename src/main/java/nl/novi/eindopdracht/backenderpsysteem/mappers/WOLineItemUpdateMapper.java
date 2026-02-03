package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.WOLineItemUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.models.WOLineItem;

public class WOLineItemUpdateMapper {
    public static WOLineItem toEntity(WOLineItemUpdateDto woLineItemUpdateDto) {
        WOLineItem WOLineItem = new WOLineItem();
        WOLineItem.setQuantity(woLineItemUpdateDto.quantity());
        WOLineItem.setReceivedQuantity(0);
        return WOLineItem;
    }
}
