package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.StockMovementOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.services.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stockmovements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<StockMovementOutputDto>> getAllStockMovements(
            @RequestParam(required = false) Long partId,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) Long purchaseOrderId,
            @RequestParam(required = false) LocalDateTime date) {
        return ResponseEntity.ok(this.service.getAllStockMovements(partId, workOrderId, purchaseOrderId, date));
    }

    @GetMapping("{id}")
    public ResponseEntity<StockMovementOutputDto> getStockMovement(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getStockMovementById(id));
    }

    @PostMapping("/goods-receipt")
    public ResponseEntity<StockMovementOutputDto> goodsReceipt(@Valid @RequestBody StockMovementInputDto stockMovementInputDto) {
        StockMovementOutputDto stockMovementOutputDto = this.service.goodsReceipt(stockMovementInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + stockMovementOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(stockMovementOutputDto);
    }

    @PostMapping("/goods-receipt-reversal")
    public ResponseEntity<StockMovementOutputDto> goodsReceiptReversal(@Valid @RequestBody StockMovementInputDto stockMovementInputDto) {
        StockMovementOutputDto stockMovementOutputDto = this.service.goodsReceiptReversal(stockMovementInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + stockMovementOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(stockMovementOutputDto);
    }

    @PostMapping("/goods-issue")
    public ResponseEntity<StockMovementOutputDto> goodsIssue(@Valid @RequestBody StockMovementInputDto stockMovementInputDto) {
        StockMovementOutputDto stockMovementOutputDto = this.service.goodsIssue(stockMovementInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + stockMovementOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(stockMovementOutputDto);
    }

    @PostMapping("/goods-issue-reversal")
    public ResponseEntity<StockMovementOutputDto> goodsIssueReversal(@Valid @RequestBody StockMovementInputDto stockMovementInputDto) {
        StockMovementOutputDto stockMovementOutputDto = this.service.goodsIssueReversal(stockMovementInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + stockMovementOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(stockMovementOutputDto);
    }
}
