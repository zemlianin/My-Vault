package org.example.controllers;

import org.example.models.*;
import org.example.services.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/secret")
public class SecretController {
    final private DataAccess dataAccess;

    @Autowired
    public SecretController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @GetMapping("/ping")
    public Integer ping(@RequestParam(value = "key") Integer key) {
        return 1 + key;
    }

    @PostMapping("/create")
    public ResponseEntity<SecretResponse> createNewSecret(@RequestBody SecretRequest request) {
        if (request.getName() == null || request.getPassword() == null) {
            var badResponse = new SecretResponse(null);
            badResponse.setDescription("One or more fields are missing. All fields are mandatory.");
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        var secret = new Secret(request);
        var secretCreated = dataAccess.addSecret(secret);
        return new ResponseEntity<>(new SecretResponse(secretCreated), HttpStatus.CREATED);
    }

    @GetMapping("/get_all")
    public ResponseEntity<ListSecretsResponse> getAll() {
        return new ResponseEntity<>(new ListSecretsResponse(dataAccess.getAllSecrets()), HttpStatus.OK);
    }

    @GetMapping("/get_all_paginate")
    public ResponseEntity<ListSecretsResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var secretsPage = dataAccess.getAllSecrets(pageable);
        return new ResponseEntity<>(new ListSecretsResponse(secretsPage.getContent()), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<SecretResponse> get(@RequestParam(value = "id") UUID id) {
        var secret = dataAccess.getSecret(id);

        if (secret.isEmpty()) {
            return new ResponseEntity<>(new SecretResponse(null), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SecretResponse(secret.get()), HttpStatus.OK);
    }

    @PatchMapping("/change")
    public ResponseEntity<SecretResponse> patch(@RequestParam(value = "id") UUID id, @RequestBody SecretRequest request) {
        var secret = dataAccess.getSecret(id);

        if (secret.isEmpty()) {
            return new ResponseEntity<>(new SecretResponse(null), HttpStatus.NOT_FOUND);
        }

        var presentSecret = secret.get();

        presentSecret.changeSecret(request);

        return new ResponseEntity<>(new SecretResponse(presentSecret), HttpStatus.OK);
    }
}