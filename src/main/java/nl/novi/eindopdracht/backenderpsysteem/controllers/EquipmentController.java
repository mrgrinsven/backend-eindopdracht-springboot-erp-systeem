package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.EquipmentOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EquipmentOutputDto> createEquipment(@Valid @RequestBody EquipmentInputDto equipmentInputDto) {
        EquipmentOutputDto equipmentOutputDto = this.service.createEquipment(equipmentInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + equipmentOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(equipmentOutputDto);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentOutputDto>> getAllEquipments() {
        return ResponseEntity.ok(this.service.getAllEquipments());
    }

    @GetMapping("{id}")
    public ResponseEntity<EquipmentOutputDto> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getEquipmentById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateEquipmentById ( @PathVariable Long id, @Valid @RequestBody EquipmentInputDto equipmentInputDto ) {
        this.service.updateEquipmentById(id, equipmentInputDto);

        return ResponseEntity.noContent().build();
    }

}
