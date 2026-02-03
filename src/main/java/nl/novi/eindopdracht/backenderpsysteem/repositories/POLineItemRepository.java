package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.POLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface POLineItemRepository extends JpaRepository<POLineItem, Long> {
}
