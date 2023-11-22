package org.example.services;

import org.example.models.HideVersionOfSecret;
import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.repositories.SecretRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecretDataAccess {
    final private SecretRepository secretRepository;

    public SecretDataAccess(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    public Secret addSecret(User user, Secret secret) {
        secret.setUser(user);
        return secretRepository.save(secret);
    }

    public void deleteAll(User user) {
        secretRepository.deleteAllByUser(user);
    }

    public void deleteAllForAdmin() {
        secretRepository.deleteAll();
    }

    public List<HideVersionOfSecret> getAllSecrets(User user) {
        var collection = secretRepository.findAllByUser(user);
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();
        return secrets;
    }

    public Page<HideVersionOfSecret> getAllSecrets(User user, PageRequest pageRequest) {
        var collection = secretRepository.findAllByUser(user, pageRequest);
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();

        return new PageImpl<>(secrets, pageRequest, secrets.size());
    }

    public Optional<Secret> getSecret(User user, UUID id) {
        return secretRepository.findByIdAndUser(id, user);
    }
}
