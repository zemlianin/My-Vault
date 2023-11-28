package org.example.models.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "secret_id")
    Secret secret;
    long created_at;
    long expires_at;
    boolean is_deleted;

    public Token() {
    }

    public Token(Secret secret, long created_at, long expires_at) {
        this.secret = secret;
        this.created_at = created_at;
        this.expires_at = expires_at;
        this.is_deleted = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
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
