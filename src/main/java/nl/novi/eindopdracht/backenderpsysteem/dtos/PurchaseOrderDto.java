package nl.novi.eindopdracht.backenderpsysteem.dtos;


import java.time.LocalDate;
import java.util.List;

public record PurchaseOrderDto(
        Long id,
        LocalDate creationDate,
        String vendorName,
        Double totalPrice,
        Boolean orderStatus,
        List<PurchaseOrderLineItemDto> items,
        String createdBy
) {}
