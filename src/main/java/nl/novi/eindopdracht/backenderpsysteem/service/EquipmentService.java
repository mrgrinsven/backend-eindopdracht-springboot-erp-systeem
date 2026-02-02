package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.EquipmentMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;
import nl.novi.eindopdracht.backenderpsysteem.repositories.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentOutputDto createEquipment(EquipmentInputDto equipmentInputDto) {
        Equipment equipment = EquipmentMapper.toEntity(equipmentInputDto);
        this.equipmentRepository.save(equipment);

        return EquipmentMapper.toDto(equipment);
    }

    public List<EquipmentOutputDto> getAllEquipments() {
        List<Equipment> equipments = this.equipmentRepository.findAll();

        return  equipments
                .stream()
                .map(EquipmentMapper::toDto)
                .toList();
    }

    public EquipmentOutputDto getEquipmentById(Long id) {
        Equipment equipment = this.equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment " + id + " not found"));

        return EquipmentMapper.toDto(equipment);
    }

    public void updateEquipmentById(Long id, EquipmentInputDto equipmentInputDto) {
        Equipment equipment = this.equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment " + id + " not found"));

        equipment.setName(equipmentInputDto.name());

        this.equipmentRepository.save(equipment);
    }
}
