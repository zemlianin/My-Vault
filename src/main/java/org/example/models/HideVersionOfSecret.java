package org.example.models;

import org.example.models.entities.Secret;

import java.util.UUID;

public class HideVersionOfSecret {
    private UUID id;
    private String name;

    public HideVersionOfSecret() {
    }

    public HideVersionOfSecret(Secret secret) {
        id = secret.getId();
        name = secret.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
