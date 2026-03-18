package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.Profile;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.exception.EmailAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        // Create the User
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password())); // hash passwords
        user.setRole(Role.ELEVE); // Default role
        user.setActive(true);
        user.setCanPublish(false);

        // Create the Profile and link it to the User
        Profile profile = new Profile();
        profile.setNomCapoeira(request.nomCapoeira());
        profile.setUser(user);

        user.setProfile(profile);

        // Save to database (Cascades to save the profile automatically)
        repository.save(user);

        // Generate and return JWT
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(LoginRequest request) {
        // Authenticate user credentials (throws exception if bad password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Fetch user from DB
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate and return JWT
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
