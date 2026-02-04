package nl.novi.eindopdracht.backenderpsysteem.controllers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.RoleDto;
import nl.novi.eindopdracht.backenderpsysteem.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok().body(this.service.getAllRoles());
    }
}
