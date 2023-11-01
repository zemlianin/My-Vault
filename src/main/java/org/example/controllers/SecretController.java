package org.example.controllers;

import org.example.models.*;
import org.example.services.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/secret")
public class SecretController {
    private DataAccess dataAccess;

    @Autowired
    public SecretController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @GetMapping("/ping")
    public Integer ping(@RequestParam(value = "key") Integer key) {
        return 1 + key;
    }

    @PostMapping("/create")
    public SecretResponse createNewSecret(@RequestBody SecretRequest request) {
        if (request.getName() == null || request.getUrl() == null || request.getPassword() == null) {
            var badResponse = new SecretResponse(HttpStatus.BAD_REQUEST, null);
            badResponse.setDescription("One or more fields are missing. All fields are mandatory.");
            return badResponse;
        }
        var secret = new Secret(request);
        dataAccess.addSecret(secret);
        return new SecretResponse(HttpStatus.CREATED, secret);
    }

    @GetMapping("/get_all")
    public ListSecretsResponse getAll() {
        return new ListSecretsResponse(dataAccess.getAllSecrets(), HttpStatus.OK);
    }

    @GetMapping("/get_all_paginate")
    public ListSecretsResponse getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var secretsPage = dataAccess.getAllSecrets(pageable);
        var response = new ListSecretsResponse(secretsPage.getContent(), HttpStatus.OK);

        return response;
    }

    @GetMapping("/get")
    public SecretResponse get(@RequestParam(value = "id") UUID id) {
        var secret = dataAccess.getSecret(id);

        if (secret.isEmpty()) {
            return new SecretResponse(HttpStatus.NOT_FOUND, null);
        }

        return new SecretResponse(HttpStatus.OK, secret.get());
    }

    @PatchMapping("/change")
    public SecretResponse patch(@RequestParam(value = "id") UUID id, @RequestBody SecretRequest request) {
        var secret = dataAccess.getSecret(id);

        if (secret.isEmpty()) {
            return new SecretResponse(HttpStatus.NOT_FOUND, null);
        }

        var presentSecret = secret.get();

        presentSecret.changeSecret(request);
        return new SecretResponse(HttpStatus.OK, presentSecret);
    }
}