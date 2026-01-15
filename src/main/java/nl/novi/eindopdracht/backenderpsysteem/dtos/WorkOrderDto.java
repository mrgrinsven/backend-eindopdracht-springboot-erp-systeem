package nl.novi.eindopdracht.backenderpsysteem.dtos;

import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;

import java.time.LocalDate;
import java.util.List;

public record WorkOrderDto(
        Long id,
        LocalDate creationDate,
        Integer quantity,
        Boolean status,
        Integer repairTime,
        Equipment equipmentId,
        List<WorkOrderLineItemDto> items,
        String createdBy
) {}
