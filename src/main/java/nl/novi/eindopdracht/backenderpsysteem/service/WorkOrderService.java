package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.WOLineItemMapper;
import nl.novi.eindopdracht.backenderpsysteem.mappers.WorkOrderMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.models.WOLineItem;
import nl.novi.eindopdracht.backenderpsysteem.models.WorkOrder;
import nl.novi.eindopdracht.backenderpsysteem.repositories.EquipmentRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.WOLineItemRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final EquipmentRepository equipmentRepository;
    private final WOLineItemRepository woLineItemRepository;
    private final PartRepository partRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, EquipmentRepository equipmentRepository, WOLineItemRepository woLineItemRepository, PartRepository partRepository) {
        this.workOrderRepository = workOrderRepository;
        this.equipmentRepository = equipmentRepository;
        this.woLineItemRepository = woLineItemRepository;
        this.partRepository = partRepository;
    }

    @Transactional
    public WorkOrderOutputDto createWorkOrder(WorkOrderInputDto workOrderInputDto) {
        WorkOrder workOrder = WorkOrderMapper.toEntity(workOrderInputDto);

        Equipment equipment = this.equipmentRepository.findById(workOrderInputDto.equipmentId()).orElseThrow(
                () -> new ResourceNotFoundException("Equipment" + workOrderInputDto.equipmentId() + " not found"));
        workOrder.setEquipment(equipment);
        workOrder.setStatus(true);

        List<WOLineItem> items = workOrderInputDto
                .items()
                .stream()
                .map(itemDto -> {
                    WOLineItem item = WOLineItemMapper.toEntity(itemDto);

                    Part part = this.partRepository.findById(itemDto.partId()).orElseThrow(
                            () -> new ResourceNotFoundException("Part" + itemDto.partId() + " not found"));

                    item.setPart(part);
                    item.setStatus(WOLineItem.Status.OPEN);
                    item.setWorkOrder(workOrder);
                    this.woLineItemRepository.save(item);

                    return item;
                })
                .toList();

        workOrder.setItems(items);

        this.workOrderRepository.save(workOrder);

        return WorkOrderMapper.toDto(workOrder);
    }

    public List<WorkOrderOutputDto> getAllWorkOrders() {
        List<WorkOrder> workOrders = this.workOrderRepository.findAll();

        return workOrders
                .stream()
                .map(WorkOrderMapper::toDto)
                .toList();
    }

    public WorkOrderOutputDto getWorkOrderById(Long id) {
        WorkOrder workOrder = this.workOrderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("WorkOrder" + id + " not found")
        );

        return WorkOrderMapper.toDto(workOrder);
    }
}
