package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.util.List;

public record EquipmentOutputDto(
        Long id,
        String name,
        Double totalMaintenanceCost,
        Integer totalMaintenanceTime,
        String createdBy,
        List<WorkOrderOutputDto> workOrders
) {}
