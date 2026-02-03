package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PartOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartInputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;

public class PartMapper {

    public static Part toEntity(PartInputDto partInputDto) {
        Part part = new Part();
        part.setName(partInputDto.name());
        part.setPartNumber(partInputDto.partNumber());
        part.setUnitPrice(partInputDto.unitPrice());
        part.setReorderPoint(partInputDto.reorderPoint());
        part.setReorderQuantity(partInputDto.reorderQuantity());
        part.setStockQuantity(0);

        return part;
    }

    public static PartOutputDto toDto(Part part) {
        return new PartOutputDto(
                part.getId(),
                part.getName(),
                part.getPartNumber(),
                part.getStockQuantity(),
                part.getUnitPrice(),
                part.getMovingAveragePrice(),
                part.getReorderPoint(),
                part.getReorderQuantity(),
                part.getCreatedBy(),
                part.getModifiedBy()
        );
    }
}
