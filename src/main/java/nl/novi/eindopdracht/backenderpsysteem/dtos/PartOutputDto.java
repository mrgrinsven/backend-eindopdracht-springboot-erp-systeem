package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record PartOutputDto(
        Long id,
        String name,
        String partNumber,
        Integer stockQuantity,
        Double unitPrice,
        Double movingAveragePrice,
        Integer reorderPoint,
        Integer reorderQuantity,
        String createdBy,
        String modifiedBy
) {}
