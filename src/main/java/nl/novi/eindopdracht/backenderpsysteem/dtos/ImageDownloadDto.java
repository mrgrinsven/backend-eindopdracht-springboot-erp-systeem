package nl.novi.eindopdracht.backenderpsysteem.dtos;

public record ImageDownloadDto(
        byte[] bytes,
        String fileName,
        String contentType
) {}
