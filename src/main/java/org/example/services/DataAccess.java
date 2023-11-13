package org.example.services;

import org.example.models.HideVersionOfSecret;
import org.example.models.Secret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataAccess {
    final private SecretRepository secretRepository;

    public DataAccess(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    public Secret addSecret(Secret secret) {
        return secretRepository.save(secret);
    }

    public void deleteAll() {
        secretRepository.deleteAll();
    }

    public List<HideVersionOfSecret> getAllSecrets() {
        var collection = secretRepository.findAll();
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new).toList();
        return secrets;
    }

    public Page<HideVersionOfSecret> getAllSecrets(PageRequest pageRequest) {
        var collection = secretRepository.findAll(pageRequest);
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();

        return new PageImpl<>(secrets, pageRequest, secrets.size());
    }

    public Optional<Secret> getSecret(UUID id) {
        return secretRepository.findById(id);
    }
}
