package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    private Boolean orderStatus;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
    private List<Part> partList;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
    private List<StockMovement> movements;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<PurchaseOrderLineItem> items = new ArrayList<>();

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

    public Boolean getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<PurchaseOrderLineItem> getItems() {
        return this.items;
    }
}
