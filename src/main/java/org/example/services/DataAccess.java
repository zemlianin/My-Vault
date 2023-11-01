package org.example.services;

import org.example.models.HideVersionOfSecret;
import org.example.models.Secret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataAccess {
    SecretRepository secretRepository;

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
        var collection = secretRepository.findAll();
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();

        int total = secrets.size();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), total);

        List<HideVersionOfSecret> sublist = Collections.emptyList();

        if (start <= end) {
            sublist = secrets.subList(start, end);
        }

        return new PageImpl<>(sublist, pageRequest, total);
    }

    public Optional<Secret> getSecret(UUID id) {
        return secretRepository.findById(id);
    }
}
