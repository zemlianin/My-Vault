package org.example.services.dataAccess;

import org.example.models.HideVersionOfSecret;
import org.example.models.dao.request.SecretRequest;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.dao.transport.RootDirectoryTransport;
import org.example.models.dao.transport.SecretTransport;
import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.models.entities.directory.Directory;
import org.example.repositories.DirectoryRepository;
import org.example.repositories.RootDirectoryRepository;
import org.example.repositories.SecretRepository;
import org.example.repositories.TokenRepository;
import org.example.services.TokenMetricsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class SecretDataAccess {
    final private SecretRepository secretRepository;
    final private TokenRepository tokenRepository;
    final private TokenMetricsService tokenMetricsService;
    final private DirectoryDataAccess directoryDataAccess;

    public SecretDataAccess(SecretRepository secretRepository,
                            TokenRepository tokenRepository,
                            TokenMetricsService tokenMetricsService,
                            DirectoryDataAccess directoryDataAccess) {
        this.secretRepository = secretRepository;
        this.tokenRepository = tokenRepository;
        this.tokenMetricsService = tokenMetricsService;
        this.directoryDataAccess = directoryDataAccess;

    }

    public Secret addSecret(User user, UUID tokenId) {
        var tokenIsValid = tokenRepository.softDeleteById(tokenId);

        if (tokenIsValid == 0) {
            tokenMetricsService.IncrementReuseOldTokenCounter();
            throw new NoSuchElementException("Token with this id does not exist");
        }

        var token = tokenRepository.findById(tokenId);

        if (token.isEmpty()) {
            tokenMetricsService.IncrementReuseOldTokenCounter();
            throw new NoSuchElementException("Token with this id does not exist");
        }

        if (token.get().getExpiresAt() < Instant.now().getEpochSecond()) {
            tokenMetricsService.incrementOverdueTokenUseCounter();
            throw new NoSuchElementException("Token with this id does not exist");
        }

        tokenMetricsService.addTokenUsageDuration(token.get().getCreatedAt());
        var secret = secretRepository.findById(token.get().getSecret().getId());

        if (secret.isEmpty()) {
            throw new NoSuchElementException("Secret not found");
        }
        var newSecret = new Secret(secret.get());
        newSecret.setUser(user);
        return secretRepository.save(newSecret);
    }

    public SecretTransport getSecretTransport(User user, SecretRequest secretRequest) {
        Optional<Directory> directory = Optional.empty();

        if (secretRequest.getDirectoryId() != null) {
            directory = Optional.of(directoryDataAccess.getDirectoryByUserAndId(user, secretRequest.getDirectoryId()));
        }

        var rootDirectory = user.getRootDirectory();

        var secretTransport = new SecretTransport(secretRequest,
                directory.isEmpty() ? Optional.empty() : Optional.of(new DirectoryTransport(directory.get())),
                new RootDirectoryTransport(rootDirectory));
        return secretTransport;
    }

    public Secret addSecret(User user, SecretTransport secretTransport) {
        var secret = new Secret(user, secretTransport);
        return secretRepository.save(secret);
    }

    public void deleteAll(User user) {
        secretRepository.deleteAllByUser(user);
    }

    public void deleteAllForAdmin() {
        secretRepository.deleteAll();
    }

    public List<HideVersionOfSecret> getAllSecrets(User user, UUID directoryId) {
        var collection = secretRepository.findAllByUserAndDirectoryId(user, directoryId);
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();
        return secrets;
    }

    public Page<HideVersionOfSecret> getAllSecrets(User user, UUID directoryId, PageRequest pageRequest) {
        var collection = secretRepository.findAllByUserAndDirectoryId(user, directoryId, pageRequest);
        var secrets = collection.stream()
                .map(HideVersionOfSecret::new)
                .toList();

        return new PageImpl<>(secrets, pageRequest, secrets.size());
    }

    public Secret getSecret(User user, UUID id) {
        var secret = secretRepository.findByIdAndUser(id, user);

        if (secret.isEmpty()) {
            throw new NoSuchElementException();
        }

        return secret.get();
    }
}
