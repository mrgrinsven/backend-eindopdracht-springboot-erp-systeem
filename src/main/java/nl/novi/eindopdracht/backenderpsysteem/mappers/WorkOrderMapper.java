package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.WorkOrder;

public class WorkOrderMapper {
    public static WorkOrder toEntity(WorkOrderInputDto workOrderInputDto) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setRepairTime(workOrderInputDto.repairTime());

        return workOrder;
    }
    public static WorkOrderOutputDto toDto(WorkOrder workOrder) {
        return new WorkOrderOutputDto(
                workOrder.getId(),
                workOrder.getEquipment().getId(),
                workOrder.getRepairTime(),
                workOrder.getStatus(),
                workOrder.getCreationDate(),
                workOrder.getCreatedBy(),
                workOrder.getModifiedBy(),
                workOrder.getItems()
                        .stream()
                        .map(WOLineItemMapper::toDto).
                        toList()
        );
    }
}
