package org.example.services;

import org.example.models.dao.response.JwtAuthenticationResponse;
import org.example.models.dao.request.SignInRequest;
import org.example.models.dao.request.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}