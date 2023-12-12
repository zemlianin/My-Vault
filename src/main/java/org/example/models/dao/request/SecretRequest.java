package org.example.models.dao.request;

import java.util.UUID;

public class SecretRequest {
    String name;
    String url;
    String login;
    String password;
    UUID directoryId;

    public SecretRequest(String name, String url, String login, String password, UUID directoryId) {
        this.name = name;
        this.url = url;
        this.login = login;
        this.password = password;
        this.directoryId = directoryId;
    }

    public UUID getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(UUID directoryId) {
        this.directoryId = directoryId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
