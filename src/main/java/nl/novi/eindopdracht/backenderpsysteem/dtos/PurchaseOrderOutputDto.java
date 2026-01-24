package nl.novi.eindopdracht.backenderpsysteem.dtos;


import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderOutputDto(
        Long id,
        String vendorName,
        Double totalPrice,
        Boolean orderStatus,
        LocalDateTime creationDate,
        String createdBy,
        String modifiedBy,
        List<POLineItemOutputDto> items
) {}
