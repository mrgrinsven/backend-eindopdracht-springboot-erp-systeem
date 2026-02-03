package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.WorkOrderUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.service.WorkOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController {
    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WorkOrderOutputDto> createWorkOrder(@Valid @RequestBody WorkOrderInputDto workOrderInputDto) {
        WorkOrderOutputDto workOrderOutputDto = this.service.createWorkOrder(workOrderInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + workOrderOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(workOrderOutputDto);
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderOutputDto>> getAllWorkOrders(@RequestParam(required = false) Boolean isOpen) {
        return ResponseEntity.ok().body(this.service.getAllWorkOrders(isOpen));
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkOrderOutputDto> getWorkOrderById(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.service.getWorkOrderById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkOrderOutputDto> updateWorkOrderById(@PathVariable Long id, @Valid @RequestBody WorkOrderUpdateDto workOrderUpdateDto) {
        return ResponseEntity.ok(this.service.updateWorkOrderById(id, workOrderUpdateDto));
    }

    @PutMapping("{id}/close")
    public ResponseEntity<Object> closeWorkOrderById(@PathVariable Long id) {
        this.service.closeWorkOrderById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/open")
    public ResponseEntity<Object> openWorkOrderById(@PathVariable Long id) {
        this.service.openWorkOrderById(id);

        return ResponseEntity.ok().build();
    }
}
