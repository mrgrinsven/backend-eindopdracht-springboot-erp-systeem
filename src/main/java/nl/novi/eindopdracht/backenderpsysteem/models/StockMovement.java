package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_movements")
public class StockMovement extends Audit {

    public enum OrderType {
        WORK_ORDER,
        PURCHASE_ORDER
    }

    @Id
    @GeneratedValue
    Long id;
    private Integer quantity;
    private Integer type;
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
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

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }

    public Part getPart() {
        return this.part;
    }
}
