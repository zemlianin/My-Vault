package org.example.controllers;

import org.example.models.dao.request.DirectoryRequest;
import org.example.models.dao.response.DirectoryResponse;
import org.example.models.dao.response.ListSecretsResponse;
import org.example.models.dao.request.SecretRequest;
import org.example.models.dao.response.SecretResponse;
import org.example.models.entities.User;
import org.example.services.dataAccess.DirectoryDataAccess;
import org.example.services.dataAccess.SecretDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource/secret")
public class SecretController {
    final private SecretDataAccess secretDataAccess;
    final private DirectoryDataAccess directoryDataAccess;

    @Autowired
    public SecretController(SecretDataAccess secretDataAccess,
                            DirectoryDataAccess directoryDataAccess) {
        this.secretDataAccess = secretDataAccess;
        this.directoryDataAccess = directoryDataAccess;
    }

    @GetMapping("/ping")
    public Integer ping(@RequestParam(value = "key") Integer key) {
        return 1 + key;
    }

    @PostMapping("/create")
    public ResponseEntity<SecretResponse> createNewSecret(@AuthenticationPrincipal User user,
                                                          @RequestBody SecretRequest request) {
        try {
            if (request.getName() == null || request.getPassword() == null) {
                var badResponse = new SecretResponse();
                badResponse.setDescription("One or more fields are missing. All fields are mandatory.");
                return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
            }

            var secretTransport = secretDataAccess.getSecretTransport(user, request);

            var secretCreated = secretDataAccess.addSecret(user, secretTransport);
            return new ResponseEntity<>(new SecretResponse(secretCreated), HttpStatus.CREATED);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_all")
    public ResponseEntity<ListSecretsResponse> getAll(@AuthenticationPrincipal User user,
                                                      @RequestParam(value = "directoryId", required = false) UUID directoryId) {
        try {
            var secrets = secretDataAccess.getAllSecrets(user, directoryId);
            return new ResponseEntity<>(new ListSecretsResponse(secrets), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_all_paginate")
    public ResponseEntity<ListSecretsResponse> getAll(@AuthenticationPrincipal User user,
                                                      @RequestParam(value = "directoryId", required = false) UUID directoryId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        try {
            var pageable = PageRequest.of(page, size);
            var secretsPage = secretDataAccess.getAllSecrets(user, directoryId, pageable);
            return new ResponseEntity<>(new ListSecretsResponse(secretsPage.getContent()), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<SecretResponse> get(@AuthenticationPrincipal User user,
                                              @RequestParam(value = "id") UUID id) {
        try {
            var secret = secretDataAccess.getSecret(user, id);

            return new ResponseEntity<>(new SecretResponse(secret), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/change")
    public ResponseEntity<SecretResponse> patch(@AuthenticationPrincipal User user,
                                                @RequestParam(value = "id") UUID id,
                                                @RequestBody SecretRequest request) {
        try {
            var secret = secretDataAccess.getSecret(user, id);

            secret.changeSecret(request);

            return new ResponseEntity<>(new SecretResponse(secret), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/relocate")
    public ResponseEntity<SecretResponse> relocateSecret(@AuthenticationPrincipal User user,
                                                         @RequestParam(value = "secretId") UUID id,
                                                         @RequestParam(value = "directoryId") UUID directoryId) {
        try {
            var secret = secretDataAccess.getSecret(user, id);

            directoryDataAccess.relocateSecret(secret, directoryId, user);

            return new ResponseEntity<>(new SecretResponse(secret), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}