package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
public class Part extends Audit{

    @Id
    @GeneratedValue
    Long id;
    private String name;
    private String partNumber;
    private Integer stockQuantity;
    private Double unitPrice;
    private Double movingAveragePrice;
    private Integer reorderPoint;
    private Integer reorderQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY)
    private List<StockMovement> movements = new ArrayList<>();

    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY)
    private List<PurchaseOrderLineItem> purchaseOrderLineItems = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(Integer quantity) {
        this.stockQuantity = quantity;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getMovingAveragePrice() {
        return this.movingAveragePrice;
    }

    public void setMovingAveragePrice(Double movingAveragePrice) {
        this.movingAveragePrice = movingAveragePrice;
    }

    public Integer getReorderPoint() {
        return this.reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Integer getReorderQuantity() {
        return this.reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }
}
