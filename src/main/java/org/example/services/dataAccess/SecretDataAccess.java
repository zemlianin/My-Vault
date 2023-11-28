package org.example.services.dataAccess;

import org.example.models.HideVersionOfSecret;
import org.example.models.entities.Secret;
import org.example.models.entities.Token;
import org.example.models.entities.User;
import org.example.repositories.SecretRepository;
import org.example.repositories.TokenRepository;
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

    public SecretDataAccess(SecretRepository secretRepository, TokenRepository tokenRepository) {
        this.secretRepository = secretRepository;
        this.tokenRepository = tokenRepository;
    }

    public Secret addSecret(User user, UUID tokenId) {
        var tokenIsValid = tokenRepository.softDeleteById(tokenId);

        if(tokenIsValid == 0){
            throw new NoSuchElementException("Token with this id does not exist");
        }

        var token = tokenRepository.findById(tokenId);

        if(token.isEmpty() || token.get().getExpires_at() < Instant.now().getEpochSecond()){
            throw new NoSuchElementException("Token with this id does not exist");
        }

        var secret = secretRepository.findById(token.get().getSecret().getId());

        if(secret.isEmpty()){
            throw  new NoSuchElementException("Secret not found");
        }
        var newSecret = new Secret(secret.get());
        newSecret.setUser(user);
        return secretRepository.save(newSecret);
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
