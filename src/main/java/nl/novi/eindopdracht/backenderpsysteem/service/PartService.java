package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PartInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.PartMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import org.springframework.stereotype.Service;

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
        Part part = this.partRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Part " + id + " not found")
        );

        return PartMapper.toDto(part);
    }
}
