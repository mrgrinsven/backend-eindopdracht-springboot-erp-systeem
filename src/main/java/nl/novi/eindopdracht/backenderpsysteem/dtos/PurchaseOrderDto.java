package nl.novi.eindopdracht.backenderpsysteem.dtos;


import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderDto(
        Long id,
        String vendorName,
        Double totalPrice,
        Boolean orderStatus,
        List<PurchaseOrderLineItemDto> items,
        LocalDateTime creationDate,
        String createdBy,
        String modifiedBy
) {}
