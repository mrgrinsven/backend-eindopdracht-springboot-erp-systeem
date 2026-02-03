package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_movements")
public class StockMovement extends Audit {

    public enum StockMovementType {
        GOODS_RECEIPT(101, "Goods Receipt", "PURCHASE_ORDER"),
        GOODS_RECEIPT_REVERSAL(102, "Goods Receipt Reversal", "PURCHASE_ORDER"),

        GOODS_ISSUE(201, "Work Order Issue", "WORK_ORDER"),
        GOODS_ISSUE_REVERSAL(202, "Work Order Reversal", "WORK_ORDER");

        private final int stockMovementCode;
        private final String description;
        private final String orderType;

        StockMovementType(int stockMovementCode, String description, String orderType) {
            this.stockMovementCode = stockMovementCode;
            this.description = description;
            this.orderType = orderType;
        }
        public int getStockMovementCode() {
            return this.stockMovementCode;
        }
        public String getDescription() {
            return this.description;
        }
        public String getOrderType() {
            return this.orderType;
        }
    }

    @Id
    @GeneratedValue
    Long id;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private StockMovementType stockMovementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    public Long getId() {
        return this.id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public StockMovementType getStockMovementType() {
        return stockMovementType;
    }

    public void setStockMovementType(StockMovementType stockMovementType) {
        this.stockMovementType = stockMovementType;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Part getPart() {
        return this.part;
    }

    public void setPart(Part part) {
        this.part = part;
    }
}
