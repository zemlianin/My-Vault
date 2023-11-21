package org.example.services;

import org.example.models.dao.JwtAuthenticationResponse;
import org.example.models.dao.SignInRequest;
import org.example.models.dao.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}