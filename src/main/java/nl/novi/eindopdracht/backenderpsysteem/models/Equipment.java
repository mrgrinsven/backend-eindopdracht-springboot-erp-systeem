package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue
    Long id;
    private String name;
    private Double totalMaintenanceCost;
    private Integer totalMaintenanceTime;

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
