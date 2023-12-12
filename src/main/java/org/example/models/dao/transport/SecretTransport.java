package org.example.models.dao.transport;

import org.example.models.dao.request.SecretRequest;

import java.util.Optional;

public class SecretTransport {
    String name;
    String url;
    String login;
    String password;
    RootDirectoryTransport rootDirectoryTransport;
    Optional<DirectoryTransport> directoryTransport;

    public SecretTransport() {
    }

    public SecretTransport(SecretRequest secretRequest,
                           Optional<DirectoryTransport> directoryTransport,
                           RootDirectoryTransport rootDirectoryTransport) {
        name = secretRequest.getName();
        url = secretRequest.getUrl();
        login = secretRequest.getUrl();
        password = secretRequest.getPassword();
        this.directoryTransport = directoryTransport;
        this.rootDirectoryTransport = rootDirectoryTransport;
    }

    public SecretTransport(String name,
                           String url,
                           String login,
                           String password,
                           RootDirectoryTransport rootDirectoryTransport,
                           Optional<DirectoryTransport> directoryTransport) {
        this.name = name;
        this.url = url;
        this.login = login;
        this.password = password;
        this.rootDirectoryTransport = rootDirectoryTransport;
        this.directoryTransport = directoryTransport;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RootDirectoryTransport getRootDirectoryTransport() {
        return rootDirectoryTransport;
    }

    public void setRootDirectoryTransport(RootDirectoryTransport rootDirectoryTransport) {
        this.rootDirectoryTransport = rootDirectoryTransport;
    }

    public Optional<DirectoryTransport> getDirectoryTransport() {
        return directoryTransport;
    }

    public void setDirectoryTransport(Optional<DirectoryTransport> directoryTransport) {
        this.directoryTransport = directoryTransport;
    }
}
