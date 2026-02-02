package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.ImageDownloadDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ImageNotValidException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.PartMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@Service
public class PartService {
    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public PartOutputDto createPart(PartInputDto partInputDto) {
        Part part = PartMapper.toEntity(partInputDto);
        this.partRepository.save(part);

        return PartMapper.toDto(part);
    }

    public List<PartOutputDto> getAllParts() {
        List<Part> parts = this.partRepository.findAll();

        return parts
                .stream()
                .map(PartMapper::toDto)
                .toList();
    }

    public PartOutputDto getPartById(Long id) {
        Part part = this.partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part " + id + " not found"));

        return PartMapper.toDto(part);
    }

    public ImageDownloadDto getPartImageById(Long id) throws IOException {
        Part part = this.partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part " + id + " not found"));

        byte[] image = part.getImage();
        if (image == null) {
            throw new ImageNotValidException("No Image found");
        }

        String name = part.getName().replace(" ", "_");
        String mime = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
        if (mime == null) {
            mime = "application/octet-stream";
        }

        String extension = mime.contains("/") ? mime.split("/")[1] : "bin";
        String fileName = name + "." + extension;

        return new ImageDownloadDto(image, fileName, mime);
    }

    public void updatePartById(Long id, PartInputDto partInputDto) {
        Part part = this.partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part " + id + " not found"));

        part.setName(partInputDto.name());
        part.setPartNumber(partInputDto.partNumber());
        part.setUnitPrice(partInputDto.unitPrice());
        part.setReorderPoint(partInputDto.reorderPoint());
        part.setReorderQuantity(partInputDto.reorderQuantity());

        this.partRepository.save(part);
    }

    public void updateImageById(Long id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ImageNotValidException("Please select a file!");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new ImageNotValidException("Please select an image type!");
        }

        Part part = this.partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part " + id + " not found"));

        part.setImage(file.getBytes());
        this.partRepository.save(part);
    }
}
