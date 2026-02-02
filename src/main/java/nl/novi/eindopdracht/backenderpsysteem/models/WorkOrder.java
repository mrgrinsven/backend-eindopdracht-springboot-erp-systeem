package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "workOrder")
    private List<StockMovement> movements;

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WOLineItem> items = new ArrayList<>();

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

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public List<WOLineItem> getItems() {
        return this.items;
    }

    public void setItems(List<WOLineItem> items) {
        this.items = items;
    }
}
