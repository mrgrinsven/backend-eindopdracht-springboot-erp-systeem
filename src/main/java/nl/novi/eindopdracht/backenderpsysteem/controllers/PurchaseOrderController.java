package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderOutputDto> createPurchaseOrder(@Valid @RequestBody PurchaseOrderInputDto purchaseOrderInputDto) {
        PurchaseOrderOutputDto purchaseOrderOutputDto = service.createPurchaseOrder(purchaseOrderInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + purchaseOrderOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(purchaseOrderOutputDto);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderOutputDto>> getAllPurchaseOrders() {
        return ResponseEntity.ok(service.getAllPurchaseOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<PurchaseOrderOutputDto> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPurchaseOrderById(id));
    }
}
