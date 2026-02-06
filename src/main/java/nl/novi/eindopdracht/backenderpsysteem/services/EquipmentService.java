package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.EquipmentMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;
import nl.novi.eindopdracht.backenderpsysteem.repositories.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public EquipmentOutputDto createEquipment(EquipmentInputDto equipmentInputDto) {
        Equipment equipment = EquipmentMapper.toEntity(equipmentInputDto);
        this.equipmentRepository.save(equipment);

        return EquipmentMapper.toDto(equipment);
    }

    @Transactional(readOnly = true)
    public List<EquipmentOutputDto> getAllEquipments() {
        List<Equipment> equipments = this.equipmentRepository.findAll();

        return  equipments
                .stream()
                .map(EquipmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipmentOutputDto getEquipmentById(Long id) {
        Equipment equipment = getEquipmentOrThrow(id);

        return EquipmentMapper.toDto(equipment);
    }

    @Transactional
    public void updateEquipmentById(Long id, EquipmentInputDto equipmentInputDto) {
        Equipment equipment = getEquipmentOrThrow(id);

        equipment.setName(equipmentInputDto.name());

        this.equipmentRepository.save(equipment);
    }

    private Equipment getEquipmentOrThrow(Long id) {
        return this.equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment " + id + " not found"));
    }
}
