package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.POLineItemInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.POLineItemOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;

public class POLineItemMapper {

    public static POLineItem toEntity(POLineItemInputDto poLineItemInputDto) {
        POLineItem poLineItem = new POLineItem();
        poLineItem.setQuantity(poLineItemInputDto.quantity());
        poLineItem.setUnitPrice(poLineItemInputDto.unitPrice());
        poLineItem.setDeliveryDate(poLineItemInputDto.deliveryDate());

        return poLineItem;
    }

    public static POLineItemOutputDto toDto(POLineItem POLineItem) {
        return new POLineItemOutputDto(
                POLineItem.getId(),
                POLineItem.getPart().getId(),
                POLineItem.getPart().getName(),
                POLineItem.getPart().getPartNumber(),
                POLineItem.getQuantity(),
                POLineItem.getUnitPrice(),
                POLineItem.getDeliveryDate(),
                POLineItem.getDeliveryStatus()
        );
    }
}
