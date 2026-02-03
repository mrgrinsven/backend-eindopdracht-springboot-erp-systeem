package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.ImageDownloadDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PartOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.service.PartService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/parts")
public class PartController {

    private final PartService service;

    public PartController(PartService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PartOutputDto> createPart(@Valid @RequestBody PartInputDto partInputDto) {
        PartOutputDto partOutputDto = this.service.createPart(partInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + partOutputDto.id()).toUriString());

        return ResponseEntity.created(uri).body(partOutputDto);
    }

    @GetMapping
    public ResponseEntity<List<PartOutputDto>> getAllParts() {
        return ResponseEntity.ok(this.service.getAllParts());
    }

    @GetMapping("{id}")
    public ResponseEntity<PartOutputDto> getPartById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getPartById(id));
    }

    @GetMapping("{id}/download")
    public ResponseEntity<byte[]> getPartImageById(@PathVariable Long id) throws IOException {
        ImageDownloadDto dto = this.service.getPartImageById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.fileName() + "\"")
                .body(dto.bytes());
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updatePartById ( @PathVariable Long id, @Valid @RequestBody PartInputDto partInputDto ) {
        this.service.updatePartById(id, partInputDto);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/image")
    public ResponseEntity<String> updateImageById(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        this.service.updateImageById(id, file);

        return ResponseEntity.ok().body("Image saved!");
    }
}
