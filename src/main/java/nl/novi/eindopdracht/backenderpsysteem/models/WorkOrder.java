package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work_orders")
public class WorkOrder extends Audit {

    @Id
    @GeneratedValue
    Long id;
    private Boolean status;
    private Integer repairTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @OneToMany(mappedBy ="workOrder", fetch = FetchType.LAZY)
    private List<Part> partList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private List<StockMovement> movements;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "work_order_line_items",
            joinColumns = @JoinColumn(name = "work_order_id")
    )
    private List<WorkOrderLineItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return this.equipment;
    }

    public void setEquipmentId(Equipment equipmentId) {
        this.equipment = equipmentId;
    }
}
