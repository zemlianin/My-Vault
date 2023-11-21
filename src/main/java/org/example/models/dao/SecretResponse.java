package org.example.models.dao;

import org.example.models.entities.Secret;

public class SecretResponse extends BaseResponse {
    Secret secret;

    public SecretResponse() {
    }

    public SecretResponse(Secret secret) {
        this.secret = secret;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
        this.secret = secret;
    }
}
