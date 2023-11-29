package org.example.controllers;

import org.example.models.dao.response.SecretResponse;
import org.example.models.dao.response.TokenResponse;
import org.example.models.entities.User;
import org.example.services.dataAccess.SecretDataAccess;
import org.example.services.dataAccess.TokenDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource/token")
public class TokenController {
    final private SecretDataAccess secretDataAccess;
    final private TokenDataAccess tokenDataAccess;

    @Autowired
    public TokenController(SecretDataAccess secretDataAccess, TokenDataAccess tokenDataAccess) {
        this.secretDataAccess = secretDataAccess;
        this.tokenDataAccess = tokenDataAccess;
    }

    @PostMapping("/add_secret_by_token")
    public ResponseEntity<SecretResponse> createSecretByToken(@AuthenticationPrincipal User user,
                                                              @RequestParam(value = "token") UUID token_id) {
        try {
            var secretCreated = secretDataAccess.addSecret(user, token_id);
            return new ResponseEntity<>(new SecretResponse(secretCreated), HttpStatus.CREATED);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(new SecretResponse(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/share")
    public ResponseEntity<TokenResponse> share(@AuthenticationPrincipal User user,
                                               @RequestParam(value = "id") UUID id,
                                               @RequestParam(value = "days") int days) {
        try {
            var token = tokenDataAccess.addToken(user, id, (long) days * 24 * 60 * 60);

            return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(new TokenResponse(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
