package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.QuantityExceededException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.StockMovementMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.*;
import nl.novi.eindopdracht.backenderpsysteem.repositories.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        POLineItem poLineItem = this.poLineItemRepository.findById(stockMovementInputDto.lineItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Line Item "
                        + stockMovementInputDto.lineItemId() + " not found"));

        int quantityOrdered = poLineItem.getQuantity();
        int quantityAlreadyReceived = poLineItem.getReceivedQuantity();
        int quantityAllowed = quantityOrdered - quantityAlreadyReceived;
        int quantityMovement = stockMovementInputDto.quantity();

        if (quantityMovement > quantityAllowed) {
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

        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(poLineItem.getPurchaseOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order "
                        + poLineItem.getPurchaseOrder().getId() + " not found"));
        stockMovement.setPurchaseOrder(purchaseOrder);

        List<POLineItem.DeliveryStatus> allowedStatuses = List.of(POLineItem.DeliveryStatus.CLOSED, POLineItem.DeliveryStatus.CANCELED);
        boolean purchaseOrderCompleted = purchaseOrder
                .getItems()
                .stream()
                .allMatch(item -> allowedStatuses.contains(item.getDeliveryStatus()));

        if (purchaseOrderCompleted) {
            purchaseOrder.setIsOpen(false);
            this.purchaseOrderRepository.save(purchaseOrder);
        }

        Part part = this.partRepository.findById(poLineItem.getPart().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Part " + poLineItem.getPart().getId() + " not found"));

        double newMovingAveragePrice;
        if (part.getMovingAveragePrice() == null) {
            newMovingAveragePrice = poLineItem.getUnitPrice();
        } else {
            newMovingAveragePrice = (poLineItem.getUnitPrice() * stockMovement.getQuantity() +
                    part.getMovingAveragePrice() * part.getStockQuantity()) /
                    (stockMovement.getQuantity() + part.getStockQuantity());
        }
        part.setMovingAveragePrice(newMovingAveragePrice);

        int currentStockQuantity = part.getStockQuantity();
        int newStockQuantity = currentStockQuantity + quantityMovement;
        part.setStockQuantity(newStockQuantity);

        this.partRepository.save(part);

        stockMovement.setPart(part);

        this.stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDto(stockMovement);
    }

    @Transactional
    public StockMovementOutputDto goodsReceiptReversal(StockMovementInputDto stockMovementInputDto) {
        StockMovement stockMovement = StockMovementMapper.toEntity(stockMovementInputDto);
        stockMovement.setStockMovementType(StockMovement.StockMovementType.GOODS_RECEIPT_REVERSAL);

        POLineItem poLineItem = this.poLineItemRepository.findById(stockMovementInputDto.lineItemId()).orElseThrow(
                () -> new ResourceNotFoundException("Purchase Order Line Item " + stockMovementInputDto.lineItemId() + " not found")
        );

        int quantityAllowed = poLineItem.getReceivedQuantity();
        int quantityMovement = stockMovementInputDto.quantity();

        if (quantityMovement > quantityAllowed) {
            throw new QuantityExceededException("Quantity exceeds order limit. Maximum allowed is " + quantityAllowed);
        }

        int newReceivedQuantity = quantityAllowed - quantityMovement;
        poLineItem.setReceivedQuantity(newReceivedQuantity);

        if (newReceivedQuantity == 0) {
            poLineItem.setDeliveryStatus(POLineItem.DeliveryStatus.OPEN);
        } else {
            poLineItem.setDeliveryStatus(POLineItem.DeliveryStatus.PARTIAL);
        }
        this.poLineItemRepository.save(poLineItem);

        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(poLineItem.getPurchaseOrder().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Purchase Order " + poLineItem.getPurchaseOrder().getId() + " not found")
        );
        stockMovement.setPurchaseOrder(purchaseOrder);

        if (purchaseOrder.getIsOpen() == false) {
            purchaseOrder.setIsOpen(true);
            this.purchaseOrderRepository.save(purchaseOrder);
        }

        Part part = this.partRepository.findById(poLineItem.getPart().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Part " + poLineItem.getPart().getId() + " not found")
        );

        double newMovingAveragePrice;
        newMovingAveragePrice = (part.getMovingAveragePrice() * part.getStockQuantity() -
                poLineItem.getUnitPrice() * stockMovement.getQuantity()) /
                part.getStockQuantity() - stockMovement.getQuantity();
        part.setMovingAveragePrice(newMovingAveragePrice);

        int currentStockQuantity = part.getStockQuantity();
        int newStockQuantity = currentStockQuantity - quantityMovement;
        part.setStockQuantity(newStockQuantity);

        this.partRepository.save(part);

        stockMovement.setPart(part);

        this.stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDto(stockMovement);
    }

    @Transactional
    public StockMovementOutputDto goodsIssue(StockMovementInputDto stockMovementInputDto) {
        StockMovement stockMovement = StockMovementMapper.toEntity(stockMovementInputDto);
        stockMovement.setStockMovementType(StockMovement.StockMovementType.GOODS_ISSUE);

        WOLineItem woLineItem = this.woLineItemRepository.findById(stockMovementInputDto.lineItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Work Order Line Item "
                        + stockMovementInputDto.lineItemId() + " not found"));

        Part part = this.partRepository.findById(woLineItem.getPart().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Part " + woLineItem.getPart().getId() + " not found"));

        int quantityOrdered = woLineItem.getQuantity();
        int quantityAlreadyReceived = woLineItem.getReceivedQuantity();
        int quantityAllowed = quantityOrdered - quantityAlreadyReceived;
        int quantityMovement = stockMovementInputDto.quantity();
        if (quantityMovement > quantityAllowed) {
            throw new QuantityExceededException("Quantity exceeds stock limit. Maximum allowed is " + quantityAllowed);
        }

        double addedIssueCost = part.getMovingAveragePrice() * quantityMovement;
        woLineItem.setTotalIssuedCost(woLineItem.getTotalIssuedCost() + addedIssueCost);

        int newReceivedQuantity = quantityAlreadyReceived + quantityMovement;
        woLineItem.setReceivedQuantity(newReceivedQuantity);

        if (newReceivedQuantity == quantityOrdered) {
            woLineItem.setStatus(WOLineItem.Status.CLOSED);
        } else {
            woLineItem.setStatus(WOLineItem.Status.PARTIAL);
        }

        this.woLineItemRepository.save(woLineItem);

        int currentStockQuantity = part.getStockQuantity();
        int newStockQuantity = currentStockQuantity - quantityMovement;
        part.setStockQuantity(newStockQuantity);

        this.partRepository.save(part);

        stockMovement.setPart(part);

        WorkOrder workOrder = this.workOrderRepository.findById(woLineItem.getWorkOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Work Order "
                        + woLineItem.getWorkOrder().getId() + " not found"));
        stockMovement.setWorkOrder(workOrder);

        this.stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDto(stockMovement);
    }

    @Transactional
    public StockMovementOutputDto goodsIssueReversal(StockMovementInputDto stockMovementInputDto) {
        StockMovement stockMovement = StockMovementMapper.toEntity(stockMovementInputDto);
        stockMovement.setStockMovementType(StockMovement.StockMovementType.GOODS_ISSUE_REVERSAL);

        WOLineItem woLineItem = this.woLineItemRepository.findById(stockMovementInputDto.lineItemId()).orElseThrow(
                () -> new ResourceNotFoundException("Work Order Line Item" + stockMovementInputDto.lineItemId() + " not found")
        );

        Part part = this.partRepository.findById(woLineItem.getPart().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Part" + woLineItem.getPart().getId() + " not found")
        );

        int quantityAllowed = woLineItem.getReceivedQuantity();
        int quantityMovement = stockMovementInputDto.quantity();

        if (quantityMovement > quantityAllowed) {
            throw new QuantityExceededException("Quantity exceeds order limit. Maximum allowed is " + quantityAllowed);
        }

        if (quantityAllowed > 0) {
            double currentTotalIssuedCost = woLineItem.getTotalIssuedCost();
            double removedIssueCost = (currentTotalIssuedCost / quantityAllowed) * quantityMovement;
            woLineItem.setTotalIssuedCost(currentTotalIssuedCost - removedIssueCost);
        }

        int newReceivedQuantity = quantityAllowed - quantityMovement;
        woLineItem.setReceivedQuantity(newReceivedQuantity);

        if (newReceivedQuantity == 0) {
            woLineItem.setStatus(WOLineItem.Status.OPEN);
        } else {
            woLineItem.setStatus(WOLineItem.Status.PARTIAL);
        }

        this.woLineItemRepository.save(woLineItem);

        int currentStockQuantity = part.getStockQuantity();
        int newStockQuantity = currentStockQuantity + quantityMovement;
        part.setStockQuantity(newStockQuantity);

        this.partRepository.save(part);

        stockMovement.setPart(part);

        WorkOrder workOrder = this.workOrderRepository.findById(woLineItem.getWorkOrder().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Work Order" + woLineItem.getWorkOrder().getId() + " not found")
        );
        stockMovement.setWorkOrder(workOrder);

        this.stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDto(stockMovement);
    }

    @Transactional(readOnly = true)
    public List<StockMovementOutputDto> getAllStockMovements(Long partId, Long workOrderId, Long purchaseOrderId, LocalDateTime date) {
        Specification<StockMovement> spec = (root, query, cb) -> cb.conjunction();

        if (partId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("part").get("id"), partId));
        }
        if (workOrderId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("workOrder").get("id"), workOrderId));
        }
        if (purchaseOrderId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("purchaseOrder").get("id"), purchaseOrderId));
        }
        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("creationDate"), date));
        }

        return this.stockMovementRepository.findAll(spec)
                .stream()
                .map(StockMovementMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockMovementOutputDto getStockMovementById(Long id) {
        StockMovement stockMovement = this.stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement " + id + " not found"));

        return StockMovementMapper.toDto(stockMovement);
    }
}
