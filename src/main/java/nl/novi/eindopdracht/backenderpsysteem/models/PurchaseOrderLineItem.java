package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Embeddable
public class PurchaseOrderLineItem {

    public enum DeliveryStatus {
        OPEN,
        PARTIAL,
        CLOSED,
        CANCELED
    }

    @ManyToOne
    private Part part;
    private int quantity;
    private double unitPrice;
    private DeliveryStatus deliveryStatus;
    private LocalDate deliveryDate;

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
