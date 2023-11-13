package org.example.models;

import com.mysql.cj.exceptions.WrongArgumentException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String login;
    String url;
    String password;

    public Secret() {
    }

    public Secret(String name, String login, String url, String password) {
        this.name = name;
        this.login = login;
        this.url = url;
        this.password = password;
    }

    public Secret(SecretRequest request) {
        id = UUID.randomUUID();
        name = request.getName();
        url = request.url;
        password = request.getPassword();
        login = request.getLogin();
    }

    public void changeSecret(SecretRequest request) {
        if (request.getName() == null || request.getPassword() == null) {
            throw new WrongArgumentException();
        }
        name = request.getName();
        url = request.url;
        password = request.getPassword();
        login = request.getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
