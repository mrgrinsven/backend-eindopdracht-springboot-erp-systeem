package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends Audit {

    @Id
    @GeneratedValue
    Long id;

    private String vendorName;
    private Double totalPrice;
    private Boolean isOpen;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<StockMovement> movements;

    @OneToMany(mappedBy = "purchaseOrder",  fetch = FetchType.LAZY,  cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<POLineItem> items = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(Boolean orderStatus) {
        this.isOpen = orderStatus;
    }

    public List<POLineItem> getItems() {
        return this.items;
    }

    public void setItems(List<POLineItem> items) {
        this.items = items;
    }
}
