package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parts")
public class Part {

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
