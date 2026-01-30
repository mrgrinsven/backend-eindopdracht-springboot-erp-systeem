package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.QuantityExceededException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.StockMovementMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;
import nl.novi.eindopdracht.backenderpsysteem.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    private final PartRepository partRepository;
    private final POLineItemRepository poLineItemRepository;
    private final WOLineItemRepository woLineItemRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository,
                                PurchaseOrderRepository purchaseOrderRepository,
                                WorkOrderRepository workOrderRepository,
                                PartRepository partRepository,
                                POLineItemRepository poLineItemRepository,
                                WOLineItemRepository woLineItemRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.workOrderRepository = workOrderRepository;
        this.partRepository = partRepository;
        this.poLineItemRepository = poLineItemRepository;
        this.woLineItemRepository = woLineItemRepository;
    }

    @Transactional
    public StockMovementOutputDto goodsReceipt(StockMovementInputDto stockMovementInputDto) {
        StockMovement stockMovement = StockMovementMapper.toEntity(stockMovementInputDto);
        stockMovement.setStockMovementType(StockMovement.StockMovementType.GOODS_RECEIPT);

        POLineItem poLineItem = this.poLineItemRepository.findById(stockMovementInputDto.lineItemId()).orElseThrow(
                () -> new ResourceNotFoundException("Purchase Order Line Item" + stockMovementInputDto.lineItemId() + " not found")
        );

        int quantityOrdered = poLineItem.getQuantity();
        int quantityAlreadyReceived = poLineItem.getReceivedQuantity();
        int quantityAllowed = quantityOrdered - quantityAlreadyReceived;
        int quantityMovement = stockMovementInputDto.quantity();

        if (stockMovementInputDto.quantity() > quantityAllowed) {
            throw new QuantityExceededException("Quantity exceeds order limit. Maximum allowed is " + quantityAllowed);
        }

        int newReceivedQuantity = quantityAlreadyReceived + quantityMovement;
        poLineItem.setReceivedQuantity(newReceivedQuantity);

        if (newReceivedQuantity == quantityOrdered) {
            poLineItem.setDeliveryStatus(POLineItem.DeliveryStatus.CLOSED);
        } else {
            poLineItem.setDeliveryStatus(POLineItem.DeliveryStatus.PARTIAL);
        }
        this.poLineItemRepository.save(poLineItem);

        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(poLineItem.getPurchaseOrder().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Purchase Order" + poLineItem.getPurchaseOrder().getId() + " not found")
        );
        stockMovement.setPurchaseOrder(purchaseOrder);

        List<POLineItem.DeliveryStatus> allowedStatuses = List.of(POLineItem.DeliveryStatus.CLOSED, POLineItem.DeliveryStatus.CANCELED);
        boolean purchaseOrderCompleted = purchaseOrder
                .getItems()
                .stream()
                .allMatch(item -> allowedStatuses.contains(item.getDeliveryStatus()));

        if (purchaseOrderCompleted) {
            purchaseOrder.setOrderStatus(false);
            this.purchaseOrderRepository.save(purchaseOrder);
        }

        Part part = this.partRepository.findById(poLineItem.getPart().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Part" + poLineItem.getPart().getId() + " not found")
        );

        double newMovingAveragePrice;
        if (part.getMovingAveragePrice() == null) {
            newMovingAveragePrice = poLineItem.getUnitPrice();
        } else {
            newMovingAveragePrice = poLineItem.getUnitPrice() * stockMovement.getQuantity() +
                    part.getMovingAveragePrice() * part.getStockQuantity();
        }
        part.setMovingAveragePrice(newMovingAveragePrice);

        int newStockQuantity;
        int currentStockQuantity = part.getStockQuantity();
        if (part.getStockQuantity() == null) {
            newStockQuantity = quantityMovement;
        } else {
            newStockQuantity = currentStockQuantity + quantityMovement;
        }
        part.setStockQuantity(newStockQuantity);

        this.partRepository.save(part);

        stockMovement.setPart(part);

        this.stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDto(stockMovement);
    }

    public StockMovementOutputDto goodsReceiptReversal(StockMovementInputDto stockMovementInputDto) {

    }

    public StockMovementOutputDto goodsIssue(StockMovementInputDto stockMovementInputDto) {

    }

    public StockMovementOutputDto goodsIssueReversal(StockMovementInputDto stockMovementInputDto) {

    }

    public List<StockMovementOutputDto> getAllStockMovements() {
        List<StockMovement> stockMovements = this.stockMovementRepository.findAll();

        return stockMovements
                .stream()
                .map(StockMovementMapper::toDto)
                .toList();
    }

    public StockMovementOutputDto getStockMovementById(Long id) {
        StockMovement stockMovement = this.stockMovementRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("StockMovement" + id + " not found")
        );

        return StockMovementMapper.toDto(stockMovement);
    }
}
