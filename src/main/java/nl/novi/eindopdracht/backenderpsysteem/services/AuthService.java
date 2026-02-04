package nl.novi.eindopdracht.backenderpsysteem.services;

import nl.novi.eindopdracht.backenderpsysteem.dtos.AuthDto;
import nl.novi.eindopdracht.backenderpsysteem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public String authenticateUser(AuthDto authDto) {
        Authentication auth = this.authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password())
        );
        Object principal = auth.getPrincipal();
        if (principal == null) {
            throw new BadCredentialsException("Authentication failed: no user found ");
        }
        UserDetails ud = (UserDetails) auth.getPrincipal();
        return jwtService.generateToken(ud);
    }
}
