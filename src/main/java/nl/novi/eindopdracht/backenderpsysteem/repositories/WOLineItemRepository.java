package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.WOLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WOLineItemRepository extends JpaRepository<WOLineItem, Long> {
}
