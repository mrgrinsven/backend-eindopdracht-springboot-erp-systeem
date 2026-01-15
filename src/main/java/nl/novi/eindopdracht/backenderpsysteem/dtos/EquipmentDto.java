package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.util.List;

public record EquipmentDto(
        Long id,
        String name,
        Double totalMaintenanceCost,
        Integer totalMaintenanceTime,
        List<WorkOrderDto> workOrders,
        String createdBy
) {}
