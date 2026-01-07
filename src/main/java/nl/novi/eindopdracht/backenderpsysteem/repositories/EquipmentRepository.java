package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
