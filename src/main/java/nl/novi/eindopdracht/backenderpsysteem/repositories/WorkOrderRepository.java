package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
}
