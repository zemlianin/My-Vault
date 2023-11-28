package org.example.models.dao.response;

import org.example.models.entities.Token;

import java.util.UUID;

public class TokenResponse {
    UUID id;
    SecretResponse secret;
    long created_at;
    long expires_at;

    public TokenResponse() {
    }

    public TokenResponse(Token token){
        this.id = token.getId();
        this.secret = new SecretResponse(token.getSecret());
        this.created_at = token.getCreated_at();
        this.expires_at = token.getExpires_at();
    }

    public TokenResponse(SecretResponse secret, long created_at, long expires_at) {
        this.secret = secret;
        this.created_at = created_at;
        this.expires_at = expires_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SecretResponse getSecret() {
        return secret;
    }

    public void setSecret(SecretResponse secret) {
        this.secret = secret;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(long expires_at) {
        this.expires_at = expires_at;
    }
}
