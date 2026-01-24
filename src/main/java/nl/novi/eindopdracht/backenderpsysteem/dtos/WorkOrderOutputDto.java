package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record WorkOrderOutputDto(
        Long id,
        Long equipmentId,
        Integer repairTime,
        Boolean status,
        LocalDateTime creationDate,
        String createdBy,
        String modifiedBy,
        List<WOLineItemOutputDto> items
) {}
