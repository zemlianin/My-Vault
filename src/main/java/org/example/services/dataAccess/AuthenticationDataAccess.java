package org.example.services.dataAccess;

import org.example.enums.Role;
import org.example.models.UserBuilder;
import org.example.models.dao.response.JwtAuthenticationResponse;
import org.example.models.dao.request.SignInRequest;
import org.example.models.dao.request.SignUpRequest;
import org.example.models.entities.directory.RootDirectory;
import org.example.repositories.RootDirectoryRepository;
import org.example.repositories.UserRepository;
import org.example.services.AuthenticationService;
import org.example.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationDataAccess implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final RootDirectoryRepository rootDirectoryRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDataAccess(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtService jwtService,
                                    AuthenticationManager authenticationManager,
                                    RootDirectoryRepository rootDirectoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.rootDirectoryRepository = rootDirectoryRepository;
    }

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var rootDirectory = new RootDirectory();
        rootDirectory = rootDirectoryRepository.save(rootDirectory);

        var user = new UserBuilder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rootDirectory(rootDirectory)
                .role(Role.USER).build();
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse.Builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        var t = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse.Builder().token(jwt).build();
    }
}
