package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.RoleDto;
import nl.novi.eindopdracht.backenderpsysteem.models.Role;
import nl.novi.eindopdracht.backenderpsysteem.repositories.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should return not empty list of RoleDto's")
    void shouldGetAllRoles() {
        //Arrange
        Role role = new Role();
        ReflectionTestUtils.setField(role, "role", "ROLE_TESTROLE");

        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));

        //Act
        List<RoleDto> result = roleService.getAllRoles();


        //Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no roles exist")
    void shouldGetEmptyList() {
        //Arrange
        Mockito.when(roleRepository.findAll()).thenReturn(List.of());

        //Act
        List<RoleDto> result = roleService.getAllRoles();

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should correctly return  multiple different roles")
    void shouldGetAllRolesMultipleDifferentRoles() {
        //Arrange
        Role role1 = new Role();
        ReflectionTestUtils.setField(role1, "role", "ROLE_MANAGER");

        Role role2 = new Role();
        ReflectionTestUtils.setField(role2, "role", "ROLE_TECHNICIAN");

        Role role3 = new Role();
        ReflectionTestUtils.setField(role3, "role", "ROLE_PURCHASER");


        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role1, role2, role3));

        //Act
        List<RoleDto> result = roleService.getAllRoles();

        //Assert
        assertEquals(3, result.size());
        assertEquals("ROLE_MANAGER", result.get(0).role());
        assertEquals("ROLE_TECHNICIAN", result.get(1).role());
        assertEquals("ROLE_PURCHASER", result.get(2).role());
    }
}