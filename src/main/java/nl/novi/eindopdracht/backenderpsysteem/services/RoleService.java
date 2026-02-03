package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.RoleDto;
import nl.novi.eindopdracht.backenderpsysteem.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map (role -> new RoleDto(role.getRole())).toList();
    }
}
