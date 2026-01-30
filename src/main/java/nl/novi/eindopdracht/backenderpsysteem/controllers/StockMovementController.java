package nl.novi.eindopdracht.backenderpsysteem.controllers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("stockmovements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockMovementOutputDto>> getAllStockMovements() {
        return ResponseEntity.ok(this.service.getAllStockMovements());
    }

    @GetMapping("{id}")
    public ResponseEntity<StockMovementOutputDto> getStockMovement(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getStockMovementById(id));
    }
}
