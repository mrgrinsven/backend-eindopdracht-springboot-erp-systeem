package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.POLineItemUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;


public class POLineItemUpdateMapper {
    public static POLineItem toEntity(POLineItemUpdateDto poLineItemUpdateDto) {
        POLineItem poLineItem = new POLineItem();
        poLineItem.setQuantity(poLineItemUpdateDto.quantity());
        poLineItem.setReceivedQuantity(0);
        poLineItem.setUnitPrice(poLineItemUpdateDto.unitPrice());
        poLineItem.setDeliveryDate(poLineItemUpdateDto.deliveryDate());

        return poLineItem;
    }
}
