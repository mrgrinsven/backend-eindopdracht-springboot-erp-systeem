package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.PasswordUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.UserInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.UserOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.InvalidPasswordException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.RoleNotFoundException;
import nl.novi.eindopdracht.backenderpsysteem.exceptions.UserAlreadyExistsException;
import nl.novi.eindopdracht.backenderpsysteem.mappers.UserMapper;
import nl.novi.eindopdracht.backenderpsysteem.models.Role;
import nl.novi.eindopdracht.backenderpsysteem.models.User;
import nl.novi.eindopdracht.backenderpsysteem.repositories.RoleRepository;
import nl.novi.eindopdracht.backenderpsysteem.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String createUser(UserInputDto userInputDto) {
        if (userRepository.existsById(userInputDto.username())) {
            throw new UserAlreadyExistsException(userInputDto.username());
        }

        User newUser = UserMapper.toEntity(userInputDto);
        newUser.setPassword(this.encoder.encode(userInputDto.password()));

        for (String roleNameInput : userInputDto.roles()) {
            String fullRoleName = "ROLE_" + roleNameInput.toUpperCase();
            Role role = this.roleRepository.findById(fullRoleName)
                    .orElseThrow(() -> new RoleNotFoundException("Role " + fullRoleName + " not found"));

            newUser.getRoles().add(role);
        }
        this.userRepository.save(newUser);
        return "User created";
    }

    @Transactional(readOnly = true)
    public UserOutputDto getUserByUsername(String username) {
        User user = this.userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

        return UserMapper.toDto(user);
    }

    @Transactional
    public String changePassword(String username, PasswordUpdateDto passwordUpdateDto) {
        User user = this.userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

        if (!this.encoder.matches(passwordUpdateDto.currentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("The current password provided is invalid");
        }

        user.setPassword(this.encoder.encode(passwordUpdateDto.newPassword()));
        this.userRepository.save(user);
        return "Password changed";
    }
}
