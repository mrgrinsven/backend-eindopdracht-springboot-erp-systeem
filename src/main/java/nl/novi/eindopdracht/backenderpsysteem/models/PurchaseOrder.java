package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends Audit{

    public enum DeliveryStatus {
        OPEN,
        PARTIAL,
        CLOSED,
        CANCELED
    }

    @Id
    @GeneratedValue
    Long id;
    private LocalDate creationDate;
    private LocalDate orderDate;
    private String vendorName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDate deliveryDate;
    private DeliveryStatus status;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Part> partList;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDate orderDAte) {
        this.orderDate = orderDAte;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDAte) {
        this.deliveryDate = deliveryDAte;
    }

    public DeliveryStatus getStatus() {
        return this.status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
