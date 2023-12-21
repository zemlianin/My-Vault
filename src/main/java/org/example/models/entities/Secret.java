package org.example.models.entities;

import com.mysql.cj.exceptions.WrongArgumentException;
import jakarta.persistence.*;
import org.example.models.dao.request.SecretRequest;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.dao.transport.SecretTransport;
import org.example.models.entities.directory.Directory;
import org.example.models.entities.directory.DirectoryInterface;
import org.example.models.entities.directory.RootDirectory;

import java.util.List;
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
    @JoinColumn(name = "directory_id")
    Directory directory;

    @ManyToOne()
    @JoinColumn(name = "root_directory_id")
    RootDirectory rootDirectory;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany()
    List<Token> tokens;

    public Secret() {
    }

    public Secret(String name, String login, String url, String password) {
        this.name = name;
        this.login = login;
        this.url = url;
        this.password = password;
    }

    public Secret(Secret other) {
        this.name = other.getName();
        this.login = other.getLogin();
        this.url = other.getUrl();
        this.password = other.getPassword();
        this.directory = other.getDirectory();
        this.rootDirectory = other.getRootDirectory();
    }

    public Secret(User user, SecretTransport secretTransport) {
        name = secretTransport.getName();
        url = secretTransport.getUrl();
        password = secretTransport.getPassword();
        login = secretTransport.getLogin();
        directory = secretTransport.getDirectoryTransport().isEmpty() ? null
                : new Directory(secretTransport.getDirectoryTransport().get());
        rootDirectory = new RootDirectory(secretTransport.getRootDirectoryTransport());
        this.user = user;
    }

    public DirectoryInterface getParentDirectory() {
        if (directory != null) {
            return directory;
        } else {
            return rootDirectory;
        }
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public RootDirectory getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(RootDirectory rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
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
