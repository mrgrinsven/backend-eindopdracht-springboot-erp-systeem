package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.*;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.DeletionRestrictedException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.OrderLineImmutableException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.POLineItemMapper;
import nl.novi.eindopdracht.backenderpsysteem.mappers.POLineItemUpdateMapper;
import nl.novi.eindopdracht.backenderpsysteem.mappers.PurchaseOrderMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import nl.novi.eindopdracht.backenderpsysteem.repositories.POLineItemRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final POLineItemRepository poLineItemRepository;
    private final PartRepository partRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, POLineItemRepository poLineItemRepository,  PartRepository partRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.poLineItemRepository = poLineItemRepository;
        this.partRepository = partRepository;
    }

    @Transactional
    public PurchaseOrderOutputDto createPurchaseOrder(PurchaseOrderInputDto purchaseOrderInputDto) {
        PurchaseOrder purchaseOrder = PurchaseOrderMapper.toEntity(purchaseOrderInputDto);
        purchaseOrder.setIsOpen(true);

        List<POLineItem> items = purchaseOrderInputDto
                .items()
                .stream()
                .map(itemDto -> {
                    POLineItem item = POLineItemMapper.toEntity(itemDto);

                    Part part = this.partRepository.findById(itemDto.partId())
                            .orElseThrow(() -> new ResourceNotFoundException("Part " + itemDto.partId() + " not found"));

                    item.setPart(part);
                    item.setDeliveryStatus(POLineItem.DeliveryStatus.OPEN);
                    item.setPurchaseOrder(purchaseOrder);
                    this.poLineItemRepository.save(item);

                    return item;
                })
                .toList();

        purchaseOrder.setItems(items);

        double totalPrice = items.stream().mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).sum();
        purchaseOrder.setTotalPrice(totalPrice);

        this.purchaseOrderRepository.save(purchaseOrder);

        return PurchaseOrderMapper.toDto(purchaseOrder);
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderOutputDto> getAllPurchaseOrders(Boolean isOpen, String vendorName) {
        List<PurchaseOrder> purchaseOrders;
        if (isOpen != null && vendorName != null) {
            purchaseOrders = this.purchaseOrderRepository.findByIsOpenAndVendorNameContainingIgnoreCase(isOpen, vendorName);
        } else if (isOpen != null) {
            purchaseOrders = this.purchaseOrderRepository.findByIsOpen(isOpen);
        } else if (vendorName != null) {
            purchaseOrders = this.purchaseOrderRepository.findByVendorNameContainingIgnoreCase(vendorName);
        } else {
            purchaseOrders = this.purchaseOrderRepository.findAll();
        }

        return purchaseOrders
                .stream()
                .map(PurchaseOrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PurchaseOrderOutputDto getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder " + id + " not found"));
        return PurchaseOrderMapper.toDto(purchaseOrder);
    }

    @Transactional
    public PurchaseOrderOutputDto updatePurchaseOrderById(Long id, PurchaseOrderUpdateDto purchaseOrderUpdateDto) {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder " + id + " not found"));

        Set<Long> poLineItemIds = purchaseOrderUpdateDto
                .items()
                .stream()
                .map(POLineItemUpdateDto::id)
                .collect(Collectors.toSet());

        List<POLineItem> itemsToDelete = purchaseOrder
                .getItems()
                .stream()
                .filter(item -> !poLineItemIds.contains(item.getId()))
                .toList();

        for (POLineItem item : itemsToDelete) {
            if (item.getDeliveryStatus() != POLineItem.DeliveryStatus.OPEN) {
                throw new DeletionRestrictedException("Purchase order line item " + item.getId()
                        + " can not be deleted because there are received items on this line");
            }
        }

        purchaseOrder.getItems().removeAll(itemsToDelete);

        for (POLineItemUpdateDto itemDto : purchaseOrderUpdateDto.items()) {
            if (itemDto.id() != null) {
                POLineItem  lineItem = this.poLineItemRepository.findById(itemDto.id())
                        .orElseThrow(() -> new ResourceNotFoundException("Purchase order lineItem " + itemDto.id() + " not found"));

                if (lineItem.getDeliveryStatus() == POLineItem.DeliveryStatus.CANCELED) {
                    throw new OrderLineImmutableException("Purchase order lineItem " + itemDto.id() + " can not be modified, it has CANCELED status");
                }

                Part part = this.partRepository.findById(itemDto.partId()).orElseThrow(
                        () -> new ResourceNotFoundException("Part" + itemDto.partId() + " not found"));

                if (!Objects.equals(part.getId(), lineItem.getPart().getId())) {
                    if (lineItem.getDeliveryStatus() != POLineItem.DeliveryStatus.OPEN) {
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
                        throw new OrderLineImmutableException("Purchase order lineItem " + itemDto.id()
                                + " can not be changed if the quantity is less than the received quantity");
                    } else if (newQuantity == minimumQuantity) {
                        lineItem.setDeliveryStatus(POLineItem.DeliveryStatus.CLOSED);
                    } else if (minimumQuantity == 0) {
                        lineItem.setDeliveryStatus(POLineItem.DeliveryStatus.OPEN);
                    } else {
                        lineItem.setDeliveryStatus(POLineItem.DeliveryStatus.PARTIAL);
                    }
                    lineItem.setQuantity(newQuantity);
                }

                if (itemDto.unitPrice() != lineItem.getUnitPrice()) {
                    lineItem.setUnitPrice(itemDto.unitPrice());
                }

                if (itemDto.deliveryDate() != lineItem.getDeliveryDate()) {
                    lineItem.setDeliveryDate(itemDto.deliveryDate());
                }
                poLineItemRepository.save(lineItem);
            } else {
                POLineItem item = POLineItemUpdateMapper.toEntity(itemDto);

                Part part = this.partRepository.findById(itemDto.partId())
                        .orElseThrow(() -> new ResourceNotFoundException("Part " + itemDto.partId() + " not found"));

                item.setPart(part);
                item.setDeliveryStatus(POLineItem.DeliveryStatus.OPEN);
                item.setPurchaseOrder(purchaseOrder);
                this.poLineItemRepository.save(item);
                purchaseOrder.getItems().add(item);
            }
        }

        double totalPrice = purchaseOrder.getItems().stream().mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).sum();
        purchaseOrder.setTotalPrice(totalPrice);

        List<POLineItem.DeliveryStatus> allowedStatuses = List.of(POLineItem.DeliveryStatus.CLOSED, POLineItem.DeliveryStatus.CANCELED);
        boolean purchaseOrderCompleted = purchaseOrder
                .getItems()
                .stream()
                .allMatch(item -> allowedStatuses.contains(item.getDeliveryStatus()));

        if (purchaseOrderCompleted) {
            purchaseOrder.setIsOpen(false);
        } else {purchaseOrder.setIsOpen(true);}

        this.purchaseOrderRepository.save(purchaseOrder);

        return PurchaseOrderMapper.toDto(purchaseOrder);
    }
}
