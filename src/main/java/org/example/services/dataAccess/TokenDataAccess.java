package org.example.services.dataAccess;

import org.example.models.entities.Secret;
import org.example.models.entities.Token;
import org.example.models.entities.User;
import org.example.repositories.SecretRepository;
import org.example.repositories.TokenRepository;
import org.example.services.TokenService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TokenDataAccess implements TokenService {
    final private TokenRepository tokenRepository;
    final private SecretRepository secretRepository;

    public TokenDataAccess(TokenRepository tokenRepository, SecretRepository secretRepository) {
        this.tokenRepository = tokenRepository;
        this.secretRepository = secretRepository;
    }

    public Token addToken(User user, UUID id, long unixDuration) {
        var token = new Token();
        var secret = secretRepository.findByIdAndUser(id, user);

        if(secret.isEmpty()){
            throw new NoSuchElementException("Отсутствует такой секрет");
        }

        token.setSecret(secret.get());
        token.setCreated_at(Instant.now().getEpochSecond());
        token.setExpires_at(Instant.now().getEpochSecond() + unixDuration);

        return tokenRepository.save(token);
    }
}
