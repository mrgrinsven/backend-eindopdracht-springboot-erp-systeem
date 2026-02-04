package nl.novi.eindopdracht.backenderpsysteem.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.backenderpsysteem.dtos.PasswordUpdateDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.UserInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.UserOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.createUser(userInputDto));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserOutputDto> getUserInfo(@Valid @PathVariable String username) {
        return ResponseEntity.ok(this.service.getUserByUsername(username));
    }

    @GetMapping("/me")
    public ResponseEntity<UserOutputDto> getUserInfo(@AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(this.service.getUserByUsername(currentUser.getUsername()));
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails currentUser,
            @Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        return ResponseEntity.ok(this.service.changePassword(currentUser.getUsername(), passwordUpdateDto));
    }
}
