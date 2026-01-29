package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.time.LocalDate;

public record POLineItemInputDto(
        Long partId,
        int quantity,
        double unitPrice,
        LocalDate deliveryDate
) {}
