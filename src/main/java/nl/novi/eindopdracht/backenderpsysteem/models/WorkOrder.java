package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "work_orders")
public class WorkOrder extends Audit {

    @Id
    @GeneratedValue
    Long id;
    private LocalDate creationDate;
    private Integer quantity;
    private Boolean status;
    private Integer repairTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipmentId;

    @OneToMany(mappedBy ="workOrder", fetch = FetchType.LAZY)
    private List<Part> partList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private List<StockMovement> movements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Integer repairTime) {
        this.repairTime = repairTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Equipment getEquipmentId() {
        return this.equipmentId;
    }

    public void setEquipmentId(Equipment equipmentId) {
        this.equipmentId = equipmentId;
    }
}
