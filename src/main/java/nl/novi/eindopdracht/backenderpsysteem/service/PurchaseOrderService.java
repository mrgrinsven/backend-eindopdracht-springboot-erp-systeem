package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.POLineItemMapper;
import nl.novi.eindopdracht.backenderpsysteem.mappers.PurchaseOrderMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import nl.novi.eindopdracht.backenderpsysteem.repositories.POLineItemRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PurchaseOrderOutputDto createPurchaseOrder(PurchaseOrderInputDto purchaseOrderInputDto) {
        PurchaseOrder purchaseOrder = PurchaseOrderMapper.toEntity(purchaseOrderInputDto);
        purchaseOrder.setOrderStatus(true);

        List<POLineItem> items = purchaseOrderInputDto
                .items()
                .stream()
                .map(itemDto -> {
                    POLineItem item = POLineItemMapper.toEntity(itemDto);

                    Part part = this.partRepository.findById(itemDto.partId()).orElseThrow(
                            () -> new ResourceNotFoundException("Part" + itemDto.partId() + " not found"));

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

    public List<PurchaseOrderOutputDto> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = this.purchaseOrderRepository.findAll();

        return purchaseOrders
                .stream()
                .map(PurchaseOrderMapper::toDto)
                .toList();
    }

    public PurchaseOrderOutputDto getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("PurchaseOrder " + id + " not found"));

        return PurchaseOrderMapper.toDto(purchaseOrder);
    }
}
