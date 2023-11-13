package org.example.models;

import java.util.UUID;

public class HideVersionOfSecret {
    private UUID id;
    private String name;

    public HideVersionOfSecret() {
    }

    public HideVersionOfSecret(Secret secret) {
        id = secret.id;
        name = secret.name;
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
