package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.util.List;

public record WorkOrderInputDto(
        Long equipmentId,
        Integer repairTime,
        List<WOLineItemInputDto> items
) {}
