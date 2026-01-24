package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.WOLineItemInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WOLineItemOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.WorkOrderLineItem;

public class WorkOrderLineItemMapper {
    public static WorkOrderLineItem toEntity(WOLineItemInputDto woLineItemInputDto) {
        WorkOrderLineItem workOrderLineItem = new WorkOrderLineItem();
        workOrderLineItem.setQuantity(woLineItemInputDto.quantity());
        return workOrderLineItem;
    }

    public static WOLineItemOutputDto toDto(WorkOrderLineItem workOrderLineItem) {
        return new WOLineItemOutputDto(
                workOrderLineItem.getPart().getId(),
                workOrderLineItem.getPart().getName(),
                workOrderLineItem.getPart().getPartNumber(),
                workOrderLineItem.getQuantity()
        );
    }
}
