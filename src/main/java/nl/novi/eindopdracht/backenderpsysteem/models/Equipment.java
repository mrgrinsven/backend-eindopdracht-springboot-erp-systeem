package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment")
public class Equipment extends Audit {

    @Id
    @GeneratedValue
    Long id;
    private String name;
    private Double totalMaintenanceCost;
    private Integer totalMaintenanceTime;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    private List<WorkOrder> workOrders = new ArrayList<WorkOrder>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalMaintenanceCost() {
        return this.totalMaintenanceCost;
    }

    public void setTotalMaintenanceCost(Double totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }

    public Integer getTotalMaintenanceTime() {
        return this.totalMaintenanceTime;
    }

    public void setTotalMaintenanceTime(Integer totalMaintenanceTime) {
        this.totalMaintenanceTime = totalMaintenanceTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
