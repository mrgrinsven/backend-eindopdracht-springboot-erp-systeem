package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
