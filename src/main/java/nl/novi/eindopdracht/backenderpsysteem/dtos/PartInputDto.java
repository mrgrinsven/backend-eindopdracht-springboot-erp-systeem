package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record PartInputDto(
        String name,
        String partNumber,
        Double unitPrice,
        Integer reorderPoint,
        Integer reorderQuantity
) {}
