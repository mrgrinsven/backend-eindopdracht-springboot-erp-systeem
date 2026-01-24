package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.util.List;

public record PurchaseOrderInputDto(
        String vendorName,
        List<PurchaseOrderLineItemDto> items
) {}
