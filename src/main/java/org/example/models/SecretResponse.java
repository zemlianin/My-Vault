package org.example.models;

import org.springframework.http.HttpStatus;

public class SecretResponse extends BaseResponse {
    Secret secret;

    public SecretResponse() {
    }

    public SecretResponse(HttpStatus status, Secret secret) {
        super(status);
        this.secret = secret;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
        this.secret = secret;
    }
}