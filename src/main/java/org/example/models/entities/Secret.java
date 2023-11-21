package org.example.models.entities;

import com.mysql.cj.exceptions.WrongArgumentException;
import jakarta.persistence.*;
import org.example.models.dao.SecretRequest;

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

    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

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
        url = request.getUrl();
        password = request.getPassword();
        login = request.getLogin();
    }

    public void changeSecret(SecretRequest request) {
        if (request.getName() == null || request.getPassword() == null) {
            throw new WrongArgumentException();
        }
        name = request.getName();
        url = request.getUrl();
        password = request.getPassword();
        login = request.getLogin();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
