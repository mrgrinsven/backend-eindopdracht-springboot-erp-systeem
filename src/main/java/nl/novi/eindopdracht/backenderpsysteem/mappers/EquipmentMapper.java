package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentInputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;

public class EquipmentMapper {

    public static Equipment toEntity(EquipmentInputDto equipmentInputDto) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentInputDto.name());
        return equipment;
    }

    public static EquipmentDto toDto(Equipment equipment) {
        return new EquipmentDto(
                equipment.getId(),
                equipment.getName(),
                equipment.getTotalMaintenanceCost(),
                equipment.getTotalMaintenanceTime(),
                equipment.getWorkOrders()
                        .stream()
                        .map(WorkOrderMapper::toDto)
                        .toList(),
                equipment.getCreatedBy()
        );
    }
}
