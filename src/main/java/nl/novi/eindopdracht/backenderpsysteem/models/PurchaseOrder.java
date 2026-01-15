package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends Audit{
    @Id
    @GeneratedValue
    Long id;
    private LocalDate creationDate;
    private String vendorName;
    private Double totalPrice;
    private Boolean orderStatus;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
    private List<Part> partList;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
    private List<StockMovement> movements;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "purchase_order_line_items",
            joinColumns = @JoinColumn(name = "purchase_order_id")
    )
    private List<PurchaseOrderLineItem> items = new ArrayList<>();

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
}
