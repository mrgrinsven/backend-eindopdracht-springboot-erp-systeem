package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "work_order_line_items")
public class WorkOrderLineItem {
    @Id
    @GeneratedValue
    Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "part_id" )
    private Part part;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    public Long getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Part getPart() {
        return this.part;
    }

    public WorkOrder getWorkOrder() {
        return this.workOrder;
    }
}
