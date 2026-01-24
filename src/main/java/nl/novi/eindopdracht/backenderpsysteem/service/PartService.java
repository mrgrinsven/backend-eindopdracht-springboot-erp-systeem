package nl.novi.eindopdracht.backenderpsysteem.service;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PartDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.PartMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Part;
import nl.novi.eindopdracht.backenderpsysteem.repositories.PartRepository;
import org.springframework.stereotype.Service;

@Service
public class PartService {
    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public PartDto getPartById(long id) {
        Part part = partRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Part " + id + " not found"));

        return PartMapper.toDto(part);
    }
}
