package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
