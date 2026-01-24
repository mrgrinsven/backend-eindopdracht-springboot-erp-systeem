package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
}
