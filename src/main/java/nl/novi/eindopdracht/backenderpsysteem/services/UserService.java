package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.UserDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.RoleNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.models.Role;
import nl.novi.eindopdracht.backenderpsysteem.models.User;
import nl.novi.eindopdracht.backenderpsysteem.repositories.RoleRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public String createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.username());
        newUser.setPassword(encoder.encode(userDto.password()));

        for (String roleNameInput : userDto.roles()) {
            String fullRoleName = "ROLE_" + roleNameInput.toUpperCase();
            Role role = this.roleRepository.findById(fullRoleName)
                    .orElseThrow(() -> new RoleNotFoundException("Role " + fullRoleName + " not found"));

            newUser.getRoles().add(role);
        }
        userRepository.save(newUser);
        return "User created";
    }
}
