package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PurchaseOrderUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.services.PurchaseOrderService;
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
        PurchaseOrderOutputDto purchaseOrderOutputDto = this.service.createPurchaseOrder(purchaseOrderInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + purchaseOrderOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(purchaseOrderOutputDto);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderOutputDto>> getAllPurchaseOrders(
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(required = false) String vendorName) {
        return ResponseEntity.ok(this.service.getAllPurchaseOrders(isOpen, vendorName));
    }

    @GetMapping("{id}")
    public ResponseEntity<PurchaseOrderOutputDto> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getPurchaseOrderById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<PurchaseOrderOutputDto> updatePurchaseOrderById(@PathVariable Long id, @Valid @RequestBody PurchaseOrderUpdateDto purchaseOrderUpdateDto) {
        return ResponseEntity.ok((this.service.updatePurchaseOrderById(id, purchaseOrderUpdateDto)));
    }
}
