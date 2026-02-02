package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.*;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.DeletionRestrictedException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.OrderLineImmutableException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.WOLineItemMapper;
import nl.novi.eindopdracht.backenderpsysteem.mappers.WOLineItemUpdateMapper;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
                () -> new ResourceNotFoundException("Equipment " + workOrderInputDto.equipmentId() + " not found"));
        workOrder.setEquipment(equipment);
        workOrder.setStatus(true);

        List<WOLineItem> items = workOrderInputDto
                .items()
                .stream()
                .map(itemDto -> {
                    WOLineItem item = WOLineItemMapper.toEntity(itemDto);

                    Part part = this.partRepository.findById(itemDto.partId())
                            .orElseThrow(() -> new ResourceNotFoundException("Part " + itemDto.partId() + " not found"));

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
                () -> new ResourceNotFoundException("WorkOrder " + id + " not found")
        );

        return WorkOrderMapper.toDto(workOrder);
    }

    @Transactional
    public void updateWorkOrderById(Long id, WorkOrderUpdateDto workOrderUpdateDto) {
        WorkOrder workOrder = this.workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder " + id + " not found"));
        Equipment equipment = this.equipmentRepository.findById(workOrderUpdateDto.equipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment " + workOrderUpdateDto.equipmentId() + " not found"));

        workOrder.setRepairTime(workOrderUpdateDto.repairTime());
        workOrder.setEquipment(equipment);

        Set<Long> woLineItemIds = workOrderUpdateDto
                .items()
                .stream()
                .map(WOLineItemUpdateDto::id)
                .collect(Collectors.toSet());

        List<WOLineItem> itemsToDelete = workOrder
                .getItems()
                .stream()
                .filter(item -> !woLineItemIds.contains(item.getId()))
                .toList();

        for (WOLineItem item : itemsToDelete) {
            if (item.getStatus() != WOLineItem.Status.OPEN) {
                throw new DeletionRestrictedException("Work order line item " + item.getId()
                        + " can not be deleted because there are received items on this line");
            }
        }

        workOrder.getItems().removeAll(itemsToDelete);

        for (WOLineItemUpdateDto itemDto : workOrderUpdateDto.items()) {
            if (itemDto.id() != null) {
                WOLineItem lineItem = this.woLineItemRepository.findById(itemDto.id())
                        .orElseThrow(() -> new ResourceNotFoundException("Work order lineItem " + itemDto.id() + " not found"));

                if (lineItem.getStatus() == WOLineItem.Status.CLOSED) {
                    throw new OrderLineImmutableException("Work order lineItem " + itemDto.id() + " can not be modified, it has CLOSED status");
                }

                Part part = this.partRepository.findById(itemDto.partId()).orElseThrow(
                        () -> new ResourceNotFoundException("Part" + itemDto.partId() + " not found"));

                if (!Objects.equals(part.getId(), itemDto.partId())) {
                    if (lineItem.getStatus() != WOLineItem.Status.OPEN) {
                        throw new OrderLineImmutableException("Work order lineItem " + itemDto.id()
                                + " can not be modified, part can not be changed if the line has already items received on it");
                    }
                    lineItem.setPart(part);
                }

                int oldQuantity = lineItem.getQuantity();
                int newQuantity = itemDto.quantity();
                int minimumQuantity = lineItem.getReceivedQuantity();

                if (oldQuantity != newQuantity) {
                    if (newQuantity < minimumQuantity ) {
                        throw new OrderLineImmutableException("Work order lineItem " + itemDto.id()
                                + " can not be changed if the quantity is less than the received quantity");
                    }
                    lineItem.setQuantity(newQuantity);
                }


            } else {
                WOLineItem item = WOLineItemUpdateMapper.toEntity(itemDto);
                Part part = this.partRepository.findById(itemDto.partId())
                        .orElseThrow(() -> new ResourceNotFoundException("Part " + itemDto.partId() + " not found"));
                item.setPart(part);
                item.setStatus(WOLineItem.Status.OPEN);
                item.setWorkOrder(workOrder);
                this.woLineItemRepository.save(item);
                workOrder.getItems().add(item);
            }
        }

        this.workOrderRepository.save(workOrder);
    }
}
