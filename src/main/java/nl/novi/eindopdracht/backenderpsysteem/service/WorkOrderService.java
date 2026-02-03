package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.*;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.DeletionRestrictedException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.OrderLineImmutableException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.OrderStateConflictException;
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

        Equipment equipment = this.equipmentRepository.findById(workOrderInputDto.equipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment "
                        + workOrderInputDto.equipmentId() + " not found"));
        workOrder.setEquipment(equipment);
        workOrder.setIsOpen(true);

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

    @Transactional(readOnly = true)
    public List<WorkOrderOutputDto> getAllWorkOrders(Boolean isOpen) {
        List<WorkOrder> workOrders;

        if (isOpen != null) {
            workOrders = this.workOrderRepository.findByIsOpen(isOpen);
        } else {
            workOrders = this.workOrderRepository.findAll();
        }

        return workOrders
                .stream()
                .map(WorkOrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkOrderOutputDto getWorkOrderById(Long id) {
        WorkOrder workOrder = this.workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder " + id + " not found"));

        return WorkOrderMapper.toDto(workOrder);
    }

    @Transactional
    public WorkOrderOutputDto updateWorkOrderById(Long id, WorkOrderUpdateDto workOrderUpdateDto) {
        WorkOrder workOrder = this.workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order " + id + " not found"));

        if (!workOrder.getIsOpen()) {
            throw new OrderLineImmutableException("Work order " + id + " can not be modified order is closed");
        }

        Equipment equipment = this.equipmentRepository.findById(workOrderUpdateDto.equipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment " + workOrderUpdateDto.equipmentId() + " not found"));

        if (!workOrderUpdateDto.repairTime().equals(workOrder.getRepairTime())) {
            workOrder.setRepairTime(workOrderUpdateDto.repairTime());
        }
        if (!workOrderUpdateDto.equipmentId().equals(equipment.getId())) {
            workOrder.setEquipment(equipment);
        }

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

                Part part = this.partRepository.findById(itemDto.partId()).orElseThrow(
                        () -> new ResourceNotFoundException("Part" + itemDto.partId() + " not found"));

                if (!Objects.equals(part.getId(), lineItem.getPart().getId())) {
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
                    } else if (newQuantity == minimumQuantity) {
                        lineItem.setStatus(WOLineItem.Status.CLOSED);
                    } else if (minimumQuantity == 0) {
                        lineItem.setStatus(WOLineItem.Status.OPEN);
                    } else {
                        lineItem.setStatus(WOLineItem.Status.PARTIAL);
                    }
                    lineItem.setQuantity(newQuantity);
                }

                this.woLineItemRepository.save(lineItem);
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

        return WorkOrderMapper.toDto(workOrder);
    }

    @Transactional
    public void closeWorkOrderById(Long id) {
        WorkOrder workOrder = this.workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order " + id + " not found"));

        if (!workOrder.getIsOpen()) {
            throw new OrderStateConflictException("Work order " + id + " already closed");
        }

        WOLineItem.Status allowedStatus = WOLineItem.Status.CLOSED;
        boolean workOrderCompleted = workOrder.getItems()
                .stream()
                .allMatch(item -> allowedStatus == item.getStatus());

        if (!workOrderCompleted) {
            throw new OrderStateConflictException("Work order " + id + " has items that need to be closed");
        }

        Equipment equipment = this.equipmentRepository.findById(workOrder.getEquipment().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Equipment " + workOrder.getEquipment().getId() + " not found"));

        if (workOrder.getTotalCostAtClosure() == 0.0) {
            double newMaintenanceCost = workOrder.getItems()
                    .stream()
                    .mapToDouble(item -> {
                        double price = item.getPart().getMovingAveragePrice();
                        return item.getQuantity() * price;
                    })
                    .sum();
            workOrder.setTotalCostAtClosure(newMaintenanceCost);
        }

        double costToAdd = workOrder.getTotalCostAtClosure();
        double currentTotalMaintenanceCost = equipment.getTotalMaintenanceCost();
        equipment.setTotalMaintenanceCost(currentTotalMaintenanceCost +  costToAdd);

        int newMaintenanceTime = workOrder.getRepairTime();
        int currentTotalMaintenanceTime = equipment.getTotalMaintenanceTime();
        equipment.setTotalMaintenanceTime(newMaintenanceTime + currentTotalMaintenanceTime);

        workOrder.setIsOpen(false);

        this.equipmentRepository.save(equipment);
        this.workOrderRepository.save(workOrder);
    }

    @Transactional
    public void openWorkOrderById(Long id) {
        WorkOrder workOrder = this.workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order " + id + " not found"));

        if (workOrder.getIsOpen() == true) {
            throw new OrderStateConflictException("Work order " + id + " already open");
        }

        Equipment equipment = this.equipmentRepository.findById(workOrder.getEquipment().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Equipment " + workOrder.getEquipment().getId() + " not found"));

        Double amountToRemove = workOrder.getTotalCostAtClosure();
        Double currentTotalMaintenanceCost = equipment.getTotalMaintenanceCost();
        equipment.setTotalMaintenanceCost(currentTotalMaintenanceCost - amountToRemove);

        int timeToRemove = workOrder.getRepairTime();
        int currentTotalMaintenanceTime = equipment.getTotalMaintenanceTime();
        equipment.setTotalMaintenanceTime(currentTotalMaintenanceTime - timeToRemove);

        workOrder.setTotalCostAtClosure(0.0);
        workOrder.setIsOpen(true);

        this.equipmentRepository.save(equipment);
        this.workOrderRepository.save(workOrder);
    }
}
