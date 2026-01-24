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

    private Integer repairTime;
    private Boolean status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @OneToMany(mappedBy ="workOrder", fetch = FetchType.LAZY)
    private List<Part> partList = new ArrayList<>();

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private List<StockMovement> movements = new ArrayList<>();

    @OneToMany(mappedBy = "workOrder")
    private List<WorkOrderLineItem> items = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public Integer getRepairTime() {
        return this.repairTime;
    }

    public void setRepairTime(Integer repairTime) {
        this.repairTime = repairTime;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

    public List<WorkOrderLineItem> getItems() {
        return this.items;
    }
}
