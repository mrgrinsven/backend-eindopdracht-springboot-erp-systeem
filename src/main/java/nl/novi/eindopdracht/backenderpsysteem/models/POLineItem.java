package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_order_line_items")
public class POLineItem {

    public enum DeliveryStatus {
        OPEN,
        PARTIAL,
        CLOSED,
        CANCELED
    }

    @Id
    @GeneratedValue
    Long id;

    private Integer quantity;
    private Integer receivedQuantity;
    private Double unitPrice;
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;


    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id" )
    private PurchaseOrder purchaseOrder;

    public Long getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getReceivedQuantity() {
        return this.receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliveryStatus getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Part getPart() {
        return this.part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
