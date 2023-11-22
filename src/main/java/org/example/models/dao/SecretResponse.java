package org.example.models.dao;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.models.entities.Secret;

import java.util.UUID;

public class SecretResponse extends BaseResponse {
    UUID id;
    String name;
    String login;
    String url;
    String password;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    UserResponse user;

    public SecretResponse() {
    }

    public SecretResponse(Secret secret) {
        this.id = secret.getId();
        this.name = secret.getName();
        this.url = secret.getUrl();
        this.login = secret.getLogin();
        this.user = new UserResponse(secret.getUser());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
