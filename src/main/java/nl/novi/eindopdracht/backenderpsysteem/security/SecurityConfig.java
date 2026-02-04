package nl.novi.eindopdracht.backenderpsysteem.security;

import nl.novi.eindopdracht.backenderpsysteem.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService service, UserRepository userRepos) {
        this.jwtService = service;
        this.userRepository = userRepos;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService udService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(udService);
        auth.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(auth);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()

                        .requestMatchers(HttpMethod.POST, "/equipments").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/equipments", "equipments/{id}").hasAnyRole("MANAGER", "TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/equipments").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.POST, "/parts").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/parts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/parts/**").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.POST, "/purchase-orders").hasAnyRole("MANAGER", "PURCHASER")
                        .requestMatchers(HttpMethod.GET, "/purchase-orders/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/purchase-orders/**").hasAnyRole("MANAGER", "PURCHASER")

                        .requestMatchers(HttpMethod.POST, "/work-orders").hasAnyRole("MANAGER", "TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/work-orders/**").hasAnyRole("MANAGER", "TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/work-orders/{id}").hasAnyRole("MANAGER", "TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/work-orders/**").hasRole("MANAGER")


                        .requestMatchers(HttpMethod.POST, "/stockmovements").hasAnyRole("MANAGER", "TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/stockmovements/**").authenticated()

                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
