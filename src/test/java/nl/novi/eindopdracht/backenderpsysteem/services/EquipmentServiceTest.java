package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;
import nl.novi.eindopdracht.backenderpsysteem.repositories.EquipmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {
    @InjectMocks
    private EquipmentService service;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Test
    @DisplayName("Should return correct EquipmentOutputDto")
    void shouldCreateEquipment() throws Exception{
        //Arrange
        EquipmentInputDto equipmentInputDto = new EquipmentInputDto("Machine One");

        Equipment equipment = new Equipment();
        equipment.setName("Machine One");
        equipment.setTotalMaintenanceCost(0.0);
        equipment.setTotalMaintenanceTime(0);

        Mockito.when(this.equipmentRepository.save(any(Equipment.class))).thenAnswer(entity -> {
            Equipment equipmentEntity = entity.getArgument(0);
            ReflectionTestUtils.setField(equipmentEntity, "id", 1L);
            ReflectionTestUtils.setField(equipmentEntity, "createdBy", "testUser");
            ReflectionTestUtils.setField(equipmentEntity, "workOrders",  new ArrayList<>());
            return equipmentEntity;
        });

        //Act
        EquipmentOutputDto result = this.service.createEquipment(equipmentInputDto);

        //Assert
        assertEquals(1L , result.id());
        assertEquals("Machine One" , result.name());
        assertEquals(0.0, result.totalMaintenanceCost());
        assertEquals(0, result.totalMaintenanceTime());
        assertEquals("testUser", result.createdBy());
        assertEquals(List.of(), result.workOrders());
    }

    @Test
    @DisplayName("Should return correct list of EquipmentOutputDtos")
    void shouldGetAllEquipments() throws Exception{
        //Arrange
        Equipment equipment1 = new Equipment();
        equipment1.setName("Machine One");
        equipment1.setTotalMaintenanceCost(6.5);
        equipment1.setTotalMaintenanceTime(200);
        ReflectionTestUtils.setField(equipment1, "id", 1L);
        ReflectionTestUtils.setField(equipment1, "createdBy", "testUser");
        ReflectionTestUtils.setField(equipment1, "workOrders",  new ArrayList<>());

        Equipment equipment2 = new Equipment();
        equipment2.setName("Machine Two");
        equipment2.setTotalMaintenanceCost(8.0);
        equipment2.setTotalMaintenanceTime(300);
        ReflectionTestUtils.setField(equipment2, "id", 2L);
        ReflectionTestUtils.setField(equipment2, "createdBy", "testUser2");
        ReflectionTestUtils.setField(equipment2, "workOrders",  new ArrayList<>());

        Mockito.when(this.equipmentRepository.findAll()).thenReturn(List.of(equipment1, equipment2));

        //Act
        List<EquipmentOutputDto> result = this.service.getAllEquipments();

        //Assert
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        assertEquals("Machine One", result.get(0).name());
        assertEquals("Machine Two", result.get(1).name());

        assertEquals(6.5, result.get(0).totalMaintenanceCost());
        assertEquals(8.0, result.get(1).totalMaintenanceCost());

        assertEquals(200, result.get(0).totalMaintenanceTime());
        assertEquals(300, result.get(1).totalMaintenanceTime());

        assertEquals(200, result.get(0).totalMaintenanceTime());
        assertEquals(300, result.get(1).totalMaintenanceTime());

        assertEquals("testUser", result.get(0).createdBy());
        assertEquals("testUser2", result.get(1).createdBy());

        assertEquals(List.of(), result.get(0).workOrders());
        assertEquals(List.of(), result.get(1).workOrders());
    }

    @Test
    @DisplayName("Should return correct order")
    void shouldGetEquipmentById() throws Exception{
        //Arrange
        Equipment equipment = new Equipment();
        equipment.setName("Machine One");
        equipment.setTotalMaintenanceCost(6.5);
        equipment.setTotalMaintenanceTime(200);
        ReflectionTestUtils.setField(equipment, "id", 1L);
        ReflectionTestUtils.setField(equipment, "createdBy", "testUser");
        ReflectionTestUtils.setField(equipment, "workOrders",  new ArrayList<>());

        Mockito.when(this.equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        //Act
        EquipmentOutputDto result = this.service.getEquipmentById(1L);

        //Assert
        assertEquals(1L, result.id());
        assertEquals("Machine One", result.name());
        assertEquals(6.5, result.totalMaintenanceCost());
        assertEquals(200, result.totalMaintenanceTime());
        assertEquals("testUser", result.createdBy());
        assertTrue(result.workOrders().isEmpty());
    }

    @Test
    @DisplayName("Should update correct equipment name")
    void shouldUpdateEquipment() throws Exception{
        //Arrange
        Long equipmentId = 1L;
        EquipmentInputDto equipmentInputDto = new EquipmentInputDto("Machine Two");

        Equipment equipment = new Equipment();
        equipment.setName("Machine One");
        ReflectionTestUtils.setField(equipment, "id", 1L);

        Mockito.when(this.equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        Mockito.when(this.equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        //Act
        this.service.updateEquipmentById(equipmentId, equipmentInputDto);


        //Assert
        assertEquals(equipmentInputDto.name(), this.service.getEquipmentById(equipmentId).name());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when id does not exist")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception{
        //Arrange
        Long id = 100L;
        Mockito.when(this.equipmentRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.service.getEquipmentById(id);
        });

        //Assert
        assertEquals("Equipment " + id + " not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when updating equipment that does not exist")
    void shouldThrowExceptionWhenUpdatingEquipmentThatDoesNotExist() throws Exception{
        //Arrange
        Long id = 100L;
        EquipmentInputDto equipmentInputDto = new EquipmentInputDto("Machine One");
        Mockito.when(this.equipmentRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.service.updateEquipmentById(id, equipmentInputDto);
        });

        //Assert
        assertEquals("Equipment " + id + " not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should return empty list when no equipment exists")
    void shouldReturnEmptyListWhenEquipmentExists() throws Exception{
        //Arrange
        Mockito.when(this.equipmentRepository.findAll()).thenReturn(List.of());

        //Act
        List<EquipmentOutputDto> result = this.service.getAllEquipments();

        //Assert
        assertTrue(result.isEmpty());
    }
}